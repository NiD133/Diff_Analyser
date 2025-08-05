package org.apache.ibatis.cache.decorators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.util.concurrent.CountDownLatch;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.FifoCache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.decorators.TransactionalCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class BlockingCache_ESTest extends BlockingCache_ESTest_scaffolding {

    // Test Constants
    private static final String CACHE_ID = "testCache";
    private static final String TEST_KEY = "testKey";
    private static final String TEST_VALUE = "testValue";
    private static final long CUSTOM_TIMEOUT = 2805L;

    // ========== Basic Functionality Tests ==========

    @Test(timeout = 4000)
    public void shouldRetrieveExistingCacheEntry() throws Throwable {
        // Given: A cache with an existing entry
        PerpetualCache underlyingCache = new PerpetualCache(CACHE_ID);
        underlyingCache.putObject(TEST_KEY, TEST_VALUE);
        BlockingCache blockingCache = new BlockingCache(underlyingCache);
        
        // When: Retrieving the cached object
        Object retrievedValue = blockingCache.getObject(TEST_KEY);
        
        // Then: The correct value should be returned
        assertEquals(TEST_VALUE, retrievedValue);
    }

    @Test(timeout = 4000)
    public void shouldStoreAndRetrieveCacheEntry() throws Throwable {
        // Given: An empty blocking cache
        PerpetualCache underlyingCache = new PerpetualCache(CACHE_ID);
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Getting an object (acquires lock) then putting it
        blockingCache.getObject(softCache);
        blockingCache.putObject(softCache, null);
        
        // Then: Cache ID should be preserved
        assertEquals(CACHE_ID, blockingCache.getId());
    }

    @Test(timeout = 4000)
    public void shouldRemoveObjectAfterAcquiringLock() throws Throwable {
        // Given: A blocking cache with a lock acquired via getObject
        PerpetualCache underlyingCache = new PerpetualCache(TEST_KEY);
        BlockingCache blockingCache = new BlockingCache(underlyingCache);
        
        // When: Getting object (acquires lock) then removing it
        blockingCache.getObject(TEST_KEY);
        Object removedValue = blockingCache.removeObject(TEST_KEY);
        
        // Then: Should return null (object didn't exist)
        assertNull(removedValue);
    }

    // ========== Cache Properties Tests ==========

    @Test(timeout = 4000)
    public void shouldSetAndGetCustomTimeout() throws Throwable {
        // Given: A blocking cache with nested decorators
        PerpetualCache baseCache = new PerpetualCache("timeoutTestCache");
        ScheduledCache scheduledCache = new ScheduledCache(baseCache);
        LruCache lruCache = new LruCache(scheduledCache);
        LoggingCache loggingCache = new LoggingCache(lruCache);
        BlockingCache blockingCache = new BlockingCache(loggingCache);
        
        // When: Setting a custom timeout
        blockingCache.setTimeout(-CUSTOM_TIMEOUT);
        
        // Then: The timeout should be retrievable
        assertEquals(-CUSTOM_TIMEOUT, blockingCache.getTimeout());
    }

    @Test(timeout = 4000)
    public void shouldReturnCorrectCacheSize() throws Throwable {
        // Given: A cache with one entry
        PerpetualCache underlyingCache = new PerpetualCache("");
        underlyingCache.putObject("", "");
        BlockingCache blockingCache = new BlockingCache(underlyingCache);
        
        // When: Getting the cache size
        int size = blockingCache.getSize();
        
        // Then: Size should be 1
        assertEquals(1, size);
    }

    @Test(timeout = 4000)
    public void shouldReturnNullCacheId() throws Throwable {
        // Given: A cache with null ID
        PerpetualCache underlyingCache = new PerpetualCache(null);
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Getting the cache ID
        String cacheId = blockingCache.getId();
        
        // Then: Should return null
        assertNull(cacheId);
    }

    @Test(timeout = 4000)
    public void shouldReturnEmptyCacheId() throws Throwable {
        // Given: A cache with empty string ID
        PerpetualCache underlyingCache = new PerpetualCache("");
        BlockingCache blockingCache = new BlockingCache(underlyingCache);
        
        // When: Getting the cache ID
        String cacheId = blockingCache.getId();
        
        // Then: Should return empty string
        assertEquals("", cacheId);
    }

    @Test(timeout = 4000)
    public void shouldReturnDefaultTimeout() throws Throwable {
        // Given: A new blocking cache
        PerpetualCache underlyingCache = new PerpetualCache("defaultTimeoutTest");
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Getting the default timeout
        long timeout = blockingCache.getTimeout();
        
        // Then: Should be 0 (default)
        assertEquals(0L, timeout);
    }

    @Test(timeout = 4000)
    public void shouldSetPositiveTimeout() throws Throwable {
        // Given: A blocking cache
        PerpetualCache underlyingCache = new PerpetualCache("positiveTimeoutTest");
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Setting a positive timeout
        blockingCache.setTimeout(1L);
        
        // Then: Timeout should be set correctly
        assertEquals(1L, blockingCache.getTimeout());
    }

    // ========== Cache Operations Tests ==========

    @Test(timeout = 4000)
    public void shouldClearCacheSuccessfully() throws Throwable {
        // Given: A blocking cache with null ID (edge case)
        PerpetualCache underlyingCache = new PerpetualCache(null);
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Clearing the cache
        blockingCache.clear();
        
        // Then: Should complete without error and maintain default timeout
        assertEquals(0L, blockingCache.getTimeout());
    }

    @Test(timeout = 4000)
    public void shouldReturnZeroSizeForEmptyCache() throws Throwable {
        // Given: An empty blocking cache
        PerpetualCache underlyingCache = new PerpetualCache("emptyCacheTest");
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Getting the size
        int size = blockingCache.getSize();
        
        // Then: Should be 0
        assertEquals(0, size);
    }

    @Test(timeout = 4000)
    public void shouldReturnCorrectCacheIdFromDelegate() throws Throwable {
        // Given: A blocking cache with a specific ID
        String expectedId = "Detected an attempt at releasing unacquired lock. This should never happen.";
        PerpetualCache underlyingCache = new PerpetualCache(expectedId);
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Getting the cache ID
        String actualId = blockingCache.getId();
        
        // Then: Should return the expected ID
        assertEquals(expectedId, actualId);
    }

    // ========== Error Condition Tests ==========

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenReleasingUnacquiredLock() throws Throwable {
        // Given: A blocking cache with null delegate
        BlockingCache blockingCache = new BlockingCache(null);
        CountDownLatch latch = new CountDownLatch(6);
        
        // When: Attempting to put object without acquiring lock first
        // Then: Should throw IllegalStateException
        try {
            blockingCache.putObject(latch, null);
            fail("Expected IllegalStateException for releasing unacquired lock");
        } catch(IllegalStateException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
            assertTrue(e.getMessage().contains("Detected an attempt at releasing unacquired lock"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenRemovingWithoutLock() throws Throwable {
        // Given: A blocking cache
        PerpetualCache underlyingCache = new PerpetualCache("lockTest");
        BlockingCache blockingCache = new BlockingCache(underlyingCache);
        
        // When: Attempting to remove object without acquiring lock first
        // Then: Should throw IllegalStateException
        try {
            blockingCache.removeObject("lockTest");
            fail("Expected IllegalStateException for releasing unacquired lock");
        } catch(IllegalStateException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
            assertTrue(e.getMessage().contains("Detected an attempt at releasing unacquired lock"));
        }
    }

    // ========== Null Delegate Error Tests ==========

    @Test(timeout = 4000)
    public void shouldThrowNullPointerExceptionForNullDelegateOperations() throws Throwable {
        // Given: A blocking cache with null delegate
        BlockingCache blockingCache = new BlockingCache(null);
        
        // Test various operations that should fail with null delegate
        assertThrowsNullPointerException(() -> blockingCache.removeObject(null), 
            "removeObject with null delegate should throw NPE");
        
        assertThrowsNullPointerException(() -> blockingCache.putObject(null, null), 
            "putObject with null delegate should throw NPE");
        
        assertThrowsNullPointerException(() -> blockingCache.getSize(), 
            "getSize with null delegate should throw NPE");
        
        assertThrowsNullPointerException(() -> blockingCache.getObject(null), 
            "getObject with null delegate should throw NPE");
        
        assertThrowsNullPointerException(() -> blockingCache.getId(), 
            "getId with null delegate should throw NPE");
        
        assertThrowsNullPointerException(() -> blockingCache.clear(), 
            "clear with null delegate should throw NPE");
    }

    // ========== Cache Chain Error Tests ==========

    @Test(timeout = 4000)
    public void shouldPropagateExceptionFromCacheChain() throws Throwable {
        // Given: A complex cache chain with null at the bottom
        FifoCache fifoCache = new FifoCache(null);
        SerializedCache serializedCache = new SerializedCache(fifoCache);
        LruCache lruCache = new LruCache(serializedCache);
        SynchronizedCache synchronizedCache = new SynchronizedCache(lruCache);
        BlockingCache blockingCache = new BlockingCache(synchronizedCache);
        
        // When: Attempting to get size
        // Then: Should propagate NPE from the chain
        try {
            blockingCache.getSize();
            fail("Expected NullPointerException from cache chain");
        } catch(NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.FifoCache", e);
        }
    }

    @Test(timeout = 4000)
    public void shouldHandleNullIdInCacheOperations() throws Throwable {
        // Given: A cache with null ID
        PerpetualCache underlyingCache = new PerpetualCache(null);
        BlockingCache blockingCache = new BlockingCache(underlyingCache);
        
        // Test operations that should fail due to null ID requirement
        assertThrowsRuntimeException(() -> blockingCache.removeObject(underlyingCache), 
            "Cache instances require an ID");
        
        assertThrowsRuntimeException(() -> blockingCache.putObject(underlyingCache, underlyingCache), 
            "Cache instances require an ID");
        
        assertThrowsRuntimeException(() -> blockingCache.getObject(underlyingCache), 
            "Cache instances require an ID");
    }

    @Test(timeout = 4000)
    public void shouldHandleComplexCacheChainErrors() throws Throwable {
        // Given: A complex cache chain with null delegate
        SynchronizedCache synchronizedCache = new SynchronizedCache(null);
        FifoCache fifoCache = new FifoCache(synchronizedCache);
        TransactionalCache transactionalCache = new TransactionalCache(fifoCache);
        BlockingCache blockingCache = new BlockingCache(transactionalCache);
        CountDownLatch latch = new CountDownLatch(600);
        
        // When: Attempting to get object
        // Then: Should propagate NPE from synchronized cache
        try {
            blockingCache.getObject(latch);
            fail("Expected NullPointerException from cache chain");
        } catch(NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SynchronizedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void shouldHandleIncompatibleCacheTypes() throws Throwable {
        // Given: A cache setup that will cause type casting issues
        String longMessage = "Detected an attempt at releasing unacquired lock. This should never happen.";
        PerpetualCache underlyingCache = new PerpetualCache(longMessage);
        SoftCache softCache = new SoftCache(underlyingCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        
        // When: Putting incompatible object type and trying to retrieve
        underlyingCache.putObject(longMessage, longMessage);
        
        // Then: Should throw ClassCastException due to SoftCache expecting SoftReference
        try {
            blockingCache.getObject(longMessage);
            fail("Expected ClassCastException due to type incompatibility");
        } catch(ClassCastException e) {
            verifyException("org.apache.ibatis.cache.decorators.SoftCache", e);
            assertTrue(e.getMessage().contains("cannot be cast to"));
        }
    }

    // ========== Helper Methods ==========

    private void assertThrowsNullPointerException(Runnable operation, String message) {
        try {
            operation.run();
            fail(message);
        } catch(NullPointerException e) {
            // Expected - verify it's from the right place
            assertTrue("NPE should be from concurrent operations", 
                e.getStackTrace()[0].getClassName().contains("java.util.concurrent") ||
                e.getStackTrace()[0].getClassName().contains("BlockingCache"));
        }
    }

    private void assertThrowsRuntimeException(Runnable operation, String expectedMessagePart) {
        try {
            operation.run();
            fail("Expected RuntimeException with message containing: " + expectedMessagePart);
        } catch(RuntimeException e) {
            assertTrue("Exception message should contain: " + expectedMessagePart, 
                e.getMessage().contains(expectedMessagePart));
            verifyException("org.apache.ibatis.cache.impl.PerpetualCache", e);
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Throwable;
    }
}