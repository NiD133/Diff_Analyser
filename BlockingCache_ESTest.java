package org.apache.ibatis.cache.decorators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Tests for BlockingCache focusing on readable, behavior-oriented scenarios.
 *
 * These tests verify:
 * - Basic delegation (id, size, clear)
 * - Timeout property
 * - Lock acquire/release protocol
 * - Concurrency behavior (thread waits for the value and resumes when it is put)
 * - Timeout when waiting for a lock
 */
public class BlockingCacheTest {

  // ---------------------------------------------------------------------------
  // Basic delegation and configuration
  // ---------------------------------------------------------------------------

  @Test
  public void returnsCachedValueWhenPresent() {
    // Arrange
    PerpetualCache delegate = new PerpetualCache("cache");
    delegate.putObject("k", "v");
    BlockingCache cache = new BlockingCache(delegate);

    // Act
    Object result = cache.getObject("k");

    // Assert
    assertEquals("v", result);
  }

  @Test
  public void exposesDelegateIdAndSize() {
    // Arrange
    PerpetualCache delegate = new PerpetualCache("myId");
    delegate.putObject("k", "v");
    BlockingCache cache = new BlockingCache(delegate);

    // Act + Assert
    assertEquals("myId", cache.getId());
    assertEquals(1, cache.getSize());
  }

  @Test
  public void clearRemovesAllEntriesFromDelegate() {
    // Arrange
    PerpetualCache delegate = new PerpetualCache("cache");
    delegate.putObject("k1", "v1");
    delegate.putObject("k2", "v2");
    BlockingCache cache = new BlockingCache(delegate);

    // Act
    cache.clear();

    // Assert
    assertEquals(0, cache.getSize());
    assertNull(cache.getObject("k1"));
    assertNull(cache.getObject("k2"));
  }

  @Test
  public void timeoutDefaultsToZeroAndCanBeConfigured() {
    // Arrange
    BlockingCache cache = new BlockingCache(new PerpetualCache("cache"));

    // Assert default
    assertEquals(0L, cache.getTimeout());

    // Act
    cache.setTimeout(250L);

    // Assert
    assertEquals(250L, cache.getTimeout());
  }

  // ---------------------------------------------------------------------------
  // Locking protocol
  // ---------------------------------------------------------------------------

  /**
   * removeObject must only be called by the thread that previously acquired the lock
   * via getObject(key) on a cache miss. Calling removeObject without having acquired
   * a lock should throw an IllegalStateException.
   */
  @Test
  public void removeWithoutAcquiredLockThrows() {
    // Arrange
    BlockingCache cache = new BlockingCache(new PerpetualCache("cache"));

    // Act + Assert
    try {
      cache.removeObject("k"); // No prior getObject("k") that acquired the lock
      fail("Expected IllegalStateException when releasing an unacquired lock");
    } catch (IllegalStateException expected) {
      // message: "Detected an attempt at releasing unacquired lock. This should never happen."
    }
  }

  // ---------------------------------------------------------------------------
  // Concurrency behavior
  // ---------------------------------------------------------------------------

  /**
   * Thread T1 gets a cache miss (acquires the lock and returns null).
   * Thread T2 then attempts to read the same key and should block.
   * When T1 puts the value, T2 should resume and obtain the value.
   */
  @Test
  public void secondThreadWaitsUntilFirstPutsValue() throws Exception {
    // Arrange
    BlockingCache cache = new BlockingCache(new PerpetualCache("cache"));

    String key = "user:42";
    String value = "Jon Snow";

    CountDownLatch t1AcquiredLock = new CountDownLatch(1);
    CountDownLatch t2Started = new CountDownLatch(1);
    CountDownLatch testDone = new CountDownLatch(1);

    // T1: simulate cache miss then populate value
    Thread t1 = new Thread(() -> {
      try {
        Object v = cache.getObject(key);
        // Miss expected on first access; lock acquired by this thread.
        assertNull(v);
        t1AcquiredLock.countDown();

        // Give T2 a moment to start and attempt to read (and thus block).
        t2Started.await(1, TimeUnit.SECONDS);
        Thread.sleep(100); // small delay to ensure T2 is waiting

        // Put the value, which should release the lock and wake T2.
        cache.putObject(key, value);
      } catch (Throwable t) {
        fail("T1 failed: " + t);
      }
    }, "T1");

    // T2: should block until T1 puts the value, then obtain it
    Thread t2 = new Thread(() -> {
      try {
        // Wait until T1 has acquired the lock on the key
        t1AcquiredLock.await(1, TimeUnit.SECONDS);
        t2Started.countDown();

        Object v = cache.getObject(key);
        assertEquals(value, v);
      } catch (Throwable t) {
        fail("T2 failed: " + t);
      } finally {
        testDone.countDown();
      }
    }, "T2");

    // Act
    t1.start();
    t2.start();

    // Assert
    if (!testDone.await(2, TimeUnit.SECONDS)) {
      fail("Test did not complete in time; possible deadlock");
    }
    t1.join(1000);
    t2.join(1000);
  }

  /**
   * When a second thread cannot obtain the lock within the configured timeout,
   * getObject should throw a CacheException.
   */
  @Test
  public void secondThreadTimesOutWhenWaitingForLock() throws Exception {
    // Arrange
    BlockingCache cache = new BlockingCache(new PerpetualCache("cache"));
    cache.setTimeout(150L); // milliseconds

    String key = "slow:key";

    CountDownLatch t1AcquiredLock = new CountDownLatch(1);
    CountDownLatch t2Attempted = new CountDownLatch(1);
    CountDownLatch testDone = new CountDownLatch(1);

    // T1: acquire the lock and hold it longer than the timeout
    Thread t1 = new Thread(() -> {
      try {
        Object v = cache.getObject(key);
        assertNull(v); // miss -> lock acquired
        t1AcquiredLock.countDown();

        // Hold the lock long enough to make T2 time out
        Thread.sleep(400);
        // Clean up: release the lock to avoid impacting other tests
        cache.removeObject(key);
      } catch (Throwable t) {
        fail("T1 failed: " + t);
      }
    }, "T1");

    // T2: should time out waiting for the lock when calling getObject
    Thread t2 = new Thread(() -> {
      try {
        t1AcquiredLock.await(1, TimeUnit.SECONDS);
        long start = System.nanoTime();
        try {
          cache.getObject(key);
          fail("Expected CacheException due to lock wait timeout");
        } catch (CacheException expected) {
          // Ensure it roughly respected the timeout (avoid exact timing assertions)
          long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
          if (elapsedMs < 120) { // some margin below 150ms
            fail("Timed out too quickly: " + elapsedMs + "ms");
          }
        }
      } catch (Throwable t) {
        fail("T2 failed: " + t);
      } finally {
        t2Attempted.countDown();
        testDone.countDown();
      }
    }, "T2");

    // Act
    t1.start();
    t2.start();

    // Assert
    if (!testDone.await(2, TimeUnit.SECONDS)) {
      fail("Test did not complete in time; possible deadlock");
    }
    t1.join(1000);
    t2.join(1000);
    if (!t2Attempted.await(0, TimeUnit.MILLISECONDS)) {
      fail("T2 did not attempt to get the object");
    }
  }
}