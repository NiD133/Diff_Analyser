package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.cache.CacheKey;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CacheKey_ESTest extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCacheKeyEqualityAfterInitialization() throws Throwable {
        CacheKey cacheKey1 = new CacheKey();
        CacheKey cacheKey2 = new CacheKey();
        assertTrue(cacheKey1.equals(cacheKey2));

        cacheKey2.update(cacheKey1);
        assertFalse(cacheKey1.equals(cacheKey2));
    }

    @Test(timeout = 4000)
    public void testUpdateCountAfterSingleUpdate() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        Object object = new Object();
        cacheKey.update(object);
        assertEquals(1, cacheKey.getUpdateCount());
    }

    @Test(timeout = 4000)
    public void testUpdateAllWithNullThrowsException() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        try {
            cacheKey.updateAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.CacheKey", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullThrowsException() throws Throwable {
        try {
            new CacheKey((Object[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.cache.CacheKey", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateAllIncreasesUpdateCount() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        Object[] objects = new Object[7];
        cacheKey.updateAll(objects);
        assertEquals(7, cacheKey.getUpdateCount());
    }

    @Test(timeout = 4000)
    public void testUpdateWithNullIncreasesUpdateCount() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(null);
        assertEquals(1, cacheKey.getUpdateCount());
    }

    @Test(timeout = 4000)
    public void testCacheKeyEqualityWithNestedCacheKey() throws Throwable {
        Object[] objects = new Object[5];
        CacheKey cacheKey = new CacheKey(objects);
        objects[2] = cacheKey;
        CacheKey cacheKey2 = new CacheKey(objects);
        assertFalse(cacheKey2.equals(objects[2]));
        assertEquals(5, cacheKey2.getUpdateCount());
    }

    @Test(timeout = 4000)
    public void testCacheKeyInequalityAfterSelfUpdate() throws Throwable {
        CacheKey cacheKey1 = new CacheKey();
        CacheKey cacheKey2 = new CacheKey(new Object[0]);
        assertTrue(cacheKey2.equals(cacheKey1));

        cacheKey2.update(cacheKey2);
        assertFalse(cacheKey2.equals(cacheKey1));
    }

    @Test(timeout = 4000)
    public void testCacheKeyInequalityWithNull() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        assertFalse(cacheKey.equals(null));
    }

    @Test(timeout = 4000)
    public void testCacheKeyEqualityWithSelf() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        assertTrue(cacheKey.equals(cacheKey));
    }

    @Test(timeout = 4000)
    public void testCacheKeyEqualityWithEmptyArray() throws Throwable {
        CacheKey cacheKey1 = new CacheKey();
        CacheKey cacheKey2 = new CacheKey(new Object[0]);
        assertTrue(cacheKey2.equals(cacheKey1));
    }

    @Test(timeout = 4000)
    public void testUpdateAllOnNullCacheKeyThrowsException() throws Throwable {
        Object[] objects = new Object[4];
        CacheKey cacheKey = new CacheKey(objects);
        try {
            cacheKey.NULL_CACHE_KEY.updateAll(objects);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.cache.CacheKey$1", e);
        }
    }

    @Test(timeout = 4000)
    public void testCacheKeyCloneEquality() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(cacheKey);
        CacheKey clonedCacheKey = cacheKey.clone();
        assertTrue(cacheKey.equals(clonedCacheKey));
        assertNotSame(clonedCacheKey, cacheKey);
    }

    @Test(timeout = 4000)
    public void testCacheKeyHashCode() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        cacheKey.hashCode(); // Just to ensure no exceptions
    }

    @Test(timeout = 4000)
    public void testCacheKeyCloneNotSameInstance() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        CacheKey clonedCacheKey = cacheKey.clone();
        assertNotSame(clonedCacheKey, cacheKey);
    }

    @Test(timeout = 4000)
    public void testCacheKeyToStringFormat() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        assertEquals("17:0", cacheKey.toString());
    }

    @Test(timeout = 4000)
    public void testInitialUpdateCountIsZero() throws Throwable {
        CacheKey cacheKey = new CacheKey();
        assertEquals(0, cacheKey.getUpdateCount());
    }
}