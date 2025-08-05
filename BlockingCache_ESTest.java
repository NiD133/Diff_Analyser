package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BlockingCache Test")
class BlockingCacheTest {

    private static final String CACHE_ID = "test-cache";
    private static final String KEY = "some_key";
    private static final String VALUE = "some_value";

    private Cache delegate;
    private BlockingCache blockingCache;

    @BeforeEach
    void setUp() {
        delegate = new PerpetualCache(CACHE_ID);
        blockingCache = new BlockingCache(delegate);
    }

    @Nested
    @DisplayName("Delegation Methods")
    class Delegation {
        @Test
        @DisplayName("should return the delegate's ID")
        void shouldReturnDelegatesId() {
            assertEquals(CACHE_ID, blockingCache.getId());
        }

        @Test
        @DisplayName("should return the delegate's size")
        void shouldReturnDelegatesSize() {
            // Given
            assertEquals(0, blockingCache.getSize());
            delegate.putObject(KEY, VALUE);

            // When & Then
            assertEquals(1, blockingCache.getSize());
        }

        @Test
        @DisplayName("should clear the delegate cache")
        void shouldClearDelegateCache() {
            // Given
            delegate.putObject(KEY, VALUE);
            assertEquals(1, blockingCache.getSize());

            // When
            blockingCache.clear();

            // Then
            assertEquals(0, blockingCache.getSize());
        }
    }

    @Nested
    @DisplayName("Core Cache Operations")
    class CoreOperations {
        @Test
        @DisplayName("should retrieve an existing item from the delegate cache")
        void shouldRetrieveExistingItem() {
            // Given
            delegate.putObject(KEY, VALUE);

            // When
            Object retrievedValue = blockingCache.getObject(KEY);

            // Then
            assertEquals(VALUE, retrievedValue);
        }

        @Test
        @DisplayName("should return null for a non-existent item and acquire a lock")
        void shouldReturnNullForMissingItemAndAcquireLock() {
            // When
            Object retrievedValue = blockingCache.getObject(KEY);

            // Then
            assertNull(retrievedValue, "Should return null for a key that is not in the cache");
            // The acquired lock is released by the subsequent putObject call.
            assertDoesNotThrow(() -> blockingCache.putObject(KEY, VALUE),
                "Putting the object should succeed by releasing the acquired lock");
        }

        @Test
        @DisplayName("should release lock when removing an object")
        void shouldReleaseLockOnRemove() {
            // Given: acquire lock by getting a non-existent key
            blockingCache.getObject(KEY);

            // When & Then: removing the object should succeed by releasing the lock
            assertDoesNotThrow(() -> blockingCache.removeObject(KEY));
        }
    }

    @Nested
    @DisplayName("Error and Edge Case Handling")
    class ErrorHandling {
        @Test
        @DisplayName("should throw IllegalStateException when putting without first acquiring a lock")
        void shouldThrowWhenPuttingWithoutLock() {
            // When & Then
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                blockingCache.putObject(KEY, VALUE);
            });
            assertTrue(exception.getMessage().contains("Detected an attempt at releasing unacquired lock"));
        }

        @Test
        @DisplayName("should throw IllegalStateException when removing without first acquiring a lock")
        void shouldThrowWhenRemovingWithoutLock() {
            // When & Then
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                blockingCache.removeObject(KEY);
            });
            assertTrue(exception.getMessage().contains("Detected an attempt at releasing unacquired lock"));
        }

        @Test
        @DisplayName("should throw NullPointerException on operations when constructed with a null delegate")
        void shouldThrowOnNullDelegate() {
            // Given
            BlockingCache cacheWithNullDelegate = new BlockingCache(null);

            // When & Then
            assertThrows(NullPointerException.class, cacheWithNullDelegate::getId);
            assertThrows(NullPointerException.class, cacheWithNullDelegate::getSize);
            assertThrows(NullPointerException.class, cacheWithNullDelegate::clear);
            assertThrows(NullPointerException.class, () -> cacheWithNullDelegate.getObject(KEY));
        }
    }

    @Nested
    @DisplayName("Timeout Behavior")
    class TimeoutBehavior {
        @Test
        @DisplayName("should get and set timeout property")
        void shouldGetAndSetTimeout() {
            // Given
            assertEquals(0L, blockingCache.getTimeout(), "Default timeout should be 0");

            // When
            blockingCache.setTimeout(5000L);

            // Then
            assertEquals(5000L, blockingCache.getTimeout());
        }

        @Test
        @DisplayName("should time out if lock is not released in time")
        @Timeout(value = 200, unit = TimeUnit.MILLISECONDS) // Test timeout to prevent it from hanging
        void shouldTimeOutWhenWaitingForLock() throws InterruptedException {
            // Given a short timeout
            blockingCache.setTimeout(50); // 50ms timeout
            final Object lockKey = "locked_key";

            // Thread 1: acquires the lock and holds it indefinitely
            Thread lockHolder = new Thread(() -> blockingCache.getObject(lockKey));
            lockHolder.start();
            lockHolder.join(); // Ensure the lock is acquired before proceeding

            // When & Then: Another attempt to get the same key should time out
            CacheException exception = assertThrows(CacheException.class, () -> {
                blockingCache.getObject(lockKey);
            });

            assertTrue(exception.getMessage().contains("could not acquire a lock on key"));
        }
    }

    @Nested
    @DisplayName("Concurrency Behavior")
    class Concurrency {
        @Test
        @DisplayName("should block a thread until another thread populates the cache")
        @Timeout(value = 1, unit = TimeUnit.SECONDS) // Test timeout to prevent hangs
        void shouldBlockOtherThreads() throws InterruptedException {
            // Given
            final Object sharedKey = "shared_key";
            final Object finalValue = "final_value";
            final CountDownLatch lockAcquiredLatch = new CountDownLatch(1);
            final AtomicBoolean waiterThreadWasBlocked = new AtomicBoolean(false);
            final AtomicBoolean waiterThreadGotCorrectValue = new AtomicBoolean(false);

            // Thread 1 (Populator): Acquires the lock, simulates work, then populates the cache.
            Thread populatorThread = new Thread(() -> {
                blockingCache.getObject(sharedKey); // Acquires lock
                lockAcquiredLatch.countDown(); // Signal that the lock is held

                try {
                    Thread.sleep(100); // Simulate a long database operation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                blockingCache.putObject(sharedKey, finalValue); // Populate cache and release lock
            });

            // Thread 2 (Waiter): Tries to get the same key and should be blocked.
            Thread waiterThread = new Thread(() -> {
                try {
                    lockAcquiredLatch.await(); // Wait until the populator has the lock
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                long startTime = System.currentTimeMillis();
                Object value = blockingCache.getObject(sharedKey); // This call should block
                long endTime = System.currentTimeMillis();

                if ((endTime - startTime) > 50) { // Check if it was blocked for a meaningful time
                    waiterThreadWasBlocked.set(true);
                }
                if (finalValue.equals(value)) { // Check if it got the correct value after unblocking
                    waiterThreadGotCorrectValue.set(true);
                }
            });

            // When
            populatorThread.start();
            waiterThread.start();

            populatorThread.join();
            waiterThread.join();

            // Then
            assertTrue(waiterThreadWasBlocked.get(), "Waiter thread should have been blocked waiting for the lock.");
            assertTrue(waiterThreadGotCorrectValue.get(), "Waiter thread should have received the value set by the populator thread.");
        }
    }
}