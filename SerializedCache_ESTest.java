package org.apache.ibatis.cache.decorators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import java.io.EOFException;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SerializedCacheTest {

    private static final String CACHE_ID = "org.apache.ibatis.cache.decorators.SerializedCache$CustomObjectInputStream";

    @Test(timeout = 4000)
    public void testPutNonSerializableObjectThrowsException() {
        PerpetualCache perpetualCache = new PerpetualCache(CACHE_ID);
        SynchronizedCache synchronizedCache = new SynchronizedCache(perpetualCache);
        SerializedCache serializedCache = new SerializedCache(synchronizedCache);

        try {
            serializedCache.putObject(CACHE_ID, perpetualCache);
            fail("Expected RuntimeException due to non-serializable object");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testHashCodeWithEmptyCache() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        serializedCache.hashCode(); // Ensure no exceptions
    }

    @Test(timeout = 4000)
    public void testCustomObjectInputStreamThrowsEOFException() {
        Enumeration<MockFileInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);

        try {
            new SerializedCache.CustomObjectInputStream(sequenceInputStream);
            fail("Expected EOFException due to empty input stream");
        } catch (EOFException e) {
            verifyException("java.io.ObjectInputStream$PeekInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testClearCache() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        serializedCache.clear();
        assertEquals("", serializedCache.getId());
    }

    @Test(timeout = 4000)
    public void testRemoveNullObject() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        perpetualCache.putObject(null, "");
        Object result = serializedCache.removeObject(null);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testGetSizeOnEmptyCache() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        int size = serializedCache.getSize();
        assertEquals(0, size);
    }

    @Test(timeout = 4000)
    public void testPutObjectIncreasesSize() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        serializedCache.putObject(perpetualCache, null);
        int size = serializedCache.getSize();
        assertEquals(1, size);
    }

    @Test(timeout = 4000)
    public void testGetIdReturnsNullForNullId() {
        PerpetualCache perpetualCache = new PerpetualCache(null);
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        String id = serializedCache.getId();
        assertNull(id);
    }

    @Test(timeout = 4000)
    public void testGetIdReturnsCorrectId() {
        PerpetualCache perpetualCache = new PerpetualCache(CACHE_ID);
        SynchronizedCache synchronizedCache = new SynchronizedCache(perpetualCache);
        SerializedCache serializedCache = new SerializedCache(synchronizedCache);
        String id = serializedCache.getId();
        assertEquals(CACHE_ID, id);
    }

    @Test(timeout = 4000)
    public void testEqualsWithTransactionalCache() {
        FifoCache fifoCache = new FifoCache(null);
        TransactionalCache transactionalCache = new TransactionalCache(fifoCache);
        SerializedCache serializedCache = new SerializedCache(transactionalCache);
        assertTrue(serializedCache.equals(transactionalCache));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentCache() {
        PerpetualCache perpetualCache = new PerpetualCache("org.apache.ibatis.exceptions.PersistenceException");
        LruCache lruCache = new LruCache(perpetualCache);
        ScheduledCache scheduledCache = new ScheduledCache(lruCache);
        WeakCache weakCache = new WeakCache(scheduledCache);
        SerializedCache serializedCache = new SerializedCache(weakCache);
        assertFalse(serializedCache.equals(perpetualCache));
    }

    @Test(timeout = 4000)
    public void testRemoveObjectFromNullCacheThrowsException() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.removeObject(null);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveObjectFromSynchronizedCacheThrowsException() {
        SynchronizedCache synchronizedCache = new SynchronizedCache(null);
        SerializedCache serializedCache = new SerializedCache(synchronizedCache);

        try {
            serializedCache.removeObject(synchronizedCache);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SynchronizedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveObjectFromBlockingCacheThrowsException() {
        PerpetualCache perpetualCache = new PerpetualCache(CACHE_ID);
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        TransactionalCache transactionalCache = new TransactionalCache(blockingCache);
        SerializedCache serializedCache = new SerializedCache(blockingCache);

        try {
            serializedCache.removeObject(transactionalCache);
            fail("Expected IllegalStateException due to unacquired lock");
        } catch (IllegalStateException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testPutObjectInSynchronizedCacheThrowsException() {
        SynchronizedCache synchronizedCache = new SynchronizedCache(null);
        SerializedCache serializedCache = new SerializedCache(synchronizedCache);

        try {
            serializedCache.putObject(null, null);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SynchronizedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testPutObjectInBlockingCacheThrowsException() {
        PerpetualCache perpetualCache = new PerpetualCache("zzCCvt");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);
        SerializedCache serializedCache = new SerializedCache(blockingCache);

        try {
            serializedCache.putObject(blockingCache, "zzCCvt");
            fail("Expected IllegalStateException due to unacquired lock");
        } catch (IllegalStateException e) {
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testHashCodeThrowsExceptionForNullId() {
        PerpetualCache perpetualCache = new PerpetualCache(null);
        SerializedCache serializedCache = new SerializedCache(perpetualCache);

        try {
            serializedCache.hashCode();
            fail("Expected RuntimeException due to null ID");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.cache.impl.PerpetualCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetSizeThrowsExceptionForNullCache() {
        SoftCache softCache = new SoftCache(null);
        SynchronizedCache synchronizedCache = new SynchronizedCache(softCache);
        SerializedCache serializedCache = new SerializedCache(synchronizedCache);

        try {
            serializedCache.getSize();
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SoftCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectThrowsExceptionForNullId() {
        PerpetualCache perpetualCache = new PerpetualCache(null);
        SerializedCache serializedCache = new SerializedCache(perpetualCache);

        try {
            serializedCache.getObject(serializedCache);
            fail("Expected RuntimeException due to null ID");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.cache.impl.PerpetualCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectFromSoftCacheThrowsException() {
        TransactionalCache transactionalCache = new TransactionalCache(null);
        SoftCache softCache = new SoftCache(transactionalCache);
        SerializedCache serializedCache = new SerializedCache(softCache);

        try {
            serializedCache.getObject(softCache);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.TransactionalCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectFromSynchronizedCacheThrowsException() {
        SynchronizedCache synchronizedCache = new SynchronizedCache(null);
        SoftCache softCache = new SoftCache(synchronizedCache);
        SerializedCache serializedCache = new SerializedCache(softCache);

        try {
            serializedCache.getObject(null);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SynchronizedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectThrowsClassCastException() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        perpetualCache.putObject("", "");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);

        try {
            serializedCache.getObject("");
            fail("Expected ClassCastException due to incorrect type");
        } catch (ClassCastException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testHashCodeThrowsExceptionForNullCache() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.hashCode();
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetObjectReturnsNullForEmptyCache() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        Object result = serializedCache.getObject("");
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testPutAndGetObject() {
        PerpetualCache perpetualCache = new PerpetualCache("");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        SerializedCache secondarySerializedCache = new SerializedCache(serializedCache);
        secondarySerializedCache.putObject(null, null);
        Object result = serializedCache.getObject(null);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetSizeThrowsExceptionForNullCache() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.getSize();
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testPutObjectThrowsExceptionForNullCache() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.putObject(null, null);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveObjectReturnsNullForNonExistentKey() {
        PerpetualCache perpetualCache = new PerpetualCache("org.apache.ibatis.exceptions.PersistenceException");
        SerializedCache serializedCache = new SerializedCache(perpetualCache);
        Object result = serializedCache.removeObject("org.apache.ibatis.exceptions.PersistenceException");
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testEqualsThrowsExceptionForNullCache() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.equals(null);
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetIdThrowsExceptionForNullCache() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.getId();
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testClearThrowsExceptionForNullCache() {
        SerializedCache serializedCache = new SerializedCache(null);

        try {
            serializedCache.clear();
            fail("Expected NullPointerException due to null cache");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }
}