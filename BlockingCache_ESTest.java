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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BlockingCache_ESTest extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testPutAndGetObject() throws Throwable {
        // Test putting and getting an object from the cache
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        perpetualCache.putObject("key", "value");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        Object retrievedObject = blockingCache.getObject("key");
        assertEquals("value", retrievedObject);
    }

    @Test(timeout = 4000)
    public void testPutObjectWithNullCache() throws Throwable {
        // Test putting an object into a null cache and expecting an IllegalStateException
        BlockingCache blockingCache = new BlockingCache(null);
        CountDownLatch latch = new CountDownLatch(6);
        try {
            blockingCache.putObject(latch, null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetIdWithEmptyCache() throws Throwable {
        // Test getting the ID of an empty cache
        PerpetualCache perpetualCache = new PerpetualCache("");
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        blockingCache.getObject(softCache);
        blockingCache.putObject(softCache, null);
        assertEquals("", blockingCache.getId());
    }

    @Test(timeout = 4000)
    public void testRemoveObject() throws Throwable {
        // Test removing an object from the cache
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        blockingCache.getObject("key");
        Object removedObject = blockingCache.removeObject("key");
        assertNull(removedObject);
    }

    @Test(timeout = 4000)
    public void testSetAndGetTimeout() throws Throwable {
        // Test setting and getting the timeout value
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        ScheduledCache scheduledCache = new ScheduledCache(perpetualCache);
        LruCache lruCache = new LruCache(scheduledCache);
        LoggingCache loggingCache = new LoggingCache(lruCache);
        BlockingCache blockingCache = new BlockingCache(loggingCache);
        blockingCache.setTimeout(-2805L);
        long timeout = blockingCache.getTimeout();
        assertEquals(-2805L, timeout);
    }

    @Test(timeout = 4000)
    public void testGetSize() throws Throwable {
        // Test getting the size of the cache
        PerpetualCache perpetualCache = new PerpetualCache("");
        perpetualCache.putObject("", "");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        int size = blockingCache.getSize();
        assertEquals(1, size);
    }

    @Test(timeout = 4000)
    public void testGetIdWithNullCache() throws Throwable {
        // Test getting the ID of a cache initialized with null
        PerpetualCache perpetualCache = new PerpetualCache(null);
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        String cacheId = blockingCache.getId();
        assertNull(cacheId);
    }

    @Test(timeout = 4000)
    public void testGetIdWithEmptyStringCache() throws Throwable {
        // Test getting the ID of a cache initialized with an empty string
        PerpetualCache perpetualCache = new PerpetualCache("");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        String cacheId = blockingCache.getId();
        assertEquals("", cacheId);
    }

    @Test(timeout = 4000)
    public void testRemoveObjectWithSerializedCache() throws Throwable {
        // Test removing an object from a serialized cache and expecting a RuntimeException
        PerpetualCache perpetualCache = new PerpetualCache(null);
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(serializedCache);
        try {
            blockingCache.removeObject(perpetualCache);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.cache.impl.PerpetualCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveObjectWithNullCache() throws Throwable {
        // Test removing an object from a null cache and expecting a NullPointerException
        BlockingCache blockingCache = new BlockingCache(null);
        try {
            blockingCache.removeObject(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.concurrent.ConcurrentHashMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testPutObjectWithNullCacheAndNullValue() throws Throwable {
        // Test putting an object into a null cache with a null value and expecting a NullPointerException
        BlockingCache blockingCache = new BlockingCache(null);
        try {
            blockingCache.putObject(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.concurrent.ConcurrentHashMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetSizeWithNullCache() throws Throwable {
        // Test getting the size of a null cache and expecting a NullPointerException
        BlockingCache blockingCache = new BlockingCache(null);
        try {
            blockingCache.getSize();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetSizeWithFifoCache() throws Throwable {
        // Test getting the size of a FIFO cache and expecting a NullPointerException
        FifoCache fifoCache = new FifoCache(null);
        SerializedCache serializedCache = new SerializedCache(fifoCache);
        LruCache lruCache = new LruCache(serializedCache);
        SynchronizedCache synchronizedCache = new SynchronizedCache(lruCache);
        BlockingCache blockingCache = new BlockingCache(synchronizedCache);
        try {
            blockingCache.getSize();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.FifoCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectWithNullCache() throws Throwable {
        // Test getting an object from a null cache and expecting a RuntimeException
        PerpetualCache perpetualCache = new PerpetualCache(null);
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        try {
            blockingCache.getObject(perpetualCache);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.cache.impl.PerpetualCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectWithNullKey() throws Throwable {
        // Test getting an object with a null key from a null cache and expecting a NullPointerException
        BlockingCache blockingCache = new BlockingCache(null);
        try {
            blockingCache.getObject(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.concurrent.ConcurrentHashMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectWithSynchronizedCache() throws Throwable {
        // Test getting an object from a synchronized cache and expecting a NullPointerException
        SynchronizedCache synchronizedCache = new SynchronizedCache(null);
        FifoCache fifoCache = new FifoCache(synchronizedCache);
        TransactionalCache transactionalCache = new TransactionalCache(fifoCache);
        BlockingCache blockingCache = new BlockingCache(transactionalCache);
        CountDownLatch latch = new CountDownLatch(600);
        try {
            blockingCache.getObject(latch);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SynchronizedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectWithSoftCache() throws Throwable {
        // Test getting an object from a soft cache and expecting a ClassCastException
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        perpetualCache.putObject("key", "value");
        try {
            blockingCache.getObject("key");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.apache.ibatis.cache.decorators.SoftCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetIdWithNullCacheAndNullValue() throws Throwable {
        // Test getting the ID of a null cache with a null value and expecting a NullPointerException
        BlockingCache blockingCache = new BlockingCache(null);
        try {
            blockingCache.getId();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testClearWithNullCache() throws Throwable {
        // Test clearing a null cache and expecting a NullPointerException
        BlockingCache blockingCache = new BlockingCache(null);
        try {
            blockingCache.clear();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetTimeoutWithSoftCache() throws Throwable {
        // Test getting the timeout value from a soft cache
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        long timeout = blockingCache.getTimeout();
        assertEquals(0L, timeout);
    }

    @Test(timeout = 4000)
    public void testRemoveObjectWithIllegalStateException() throws Throwable {
        // Test removing an object and expecting an IllegalStateException
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        try {
            blockingCache.removeObject("key");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testClearWithSoftCache() throws Throwable {
        // Test clearing a soft cache
        PerpetualCache perpetualCache = new PerpetualCache(null);
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        blockingCache.clear();
        assertEquals(0L, blockingCache.getTimeout());
    }

    @Test(timeout = 4000)
    public void testGetSizeWithSoftCache() throws Throwable {
        // Test getting the size of a soft cache
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        int size = blockingCache.getSize();
        assertEquals(0, size);
    }

    @Test(timeout = 4000)
    public void testGetIdWithSoftCache() throws Throwable {
        // Test getting the ID of a soft cache
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        String cacheId = blockingCache.getId();
        assertEquals("testCache", cacheId);
    }

    @Test(timeout = 4000)
    public void testSetAndGetTimeoutWithSoftCache() throws Throwable {
        // Test setting and getting the timeout value with a soft cache
        PerpetualCache perpetualCache = new PerpetualCache("testCache");
        SoftCache softCache = new SoftCache(perpetualCache);
        BlockingCache blockingCache = new BlockingCache(softCache);
        blockingCache.setTimeout(1L);
        long timeout = blockingCache.getTimeout();
        assertEquals(1L, timeout);
    }
}