package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for SegmentConstantPoolArrayCache class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SegmentConstantPoolArrayCacheTest extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCacheArrayWithDifferentSizes() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array1 = new String[4];
        String[] array2 = new String[5];
        IdentityHashMap<String[], SegmentConstantPoolArrayCache.CachedArray> knownArrays = new IdentityHashMap<>();
        cache.knownArrays = knownArrays;

        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(array2);
        knownArrays.put(array1, cachedArray);

        assertEquals(5, cachedArray.lastKnownSize());
        assertFalse(cache.arrayIsCached(array1));
    }

    @Test(timeout = 4000)
    public void testCacheIndexes() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[8];
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(array);

        cachedArray.cacheIndexes();

        assertEquals(8, cachedArray.lastKnownSize());
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKeyWithNullElement() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[2];
        cache.lastArray = array;
        cache.cacheArray(array);

        List<Integer> indexes = cache.indexesForArrayKey(array, array[1]);

        assertNull(indexes);
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKeyWithNullKey() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        LinkedList<Integer> emptyList = new LinkedList<>();
        cache.lastIndexes = emptyList;
        String[] array = new String[2];
        cache.lastArray = array;
        cache.cacheArray(array);

        List<Integer> indexes = cache.indexesForArrayKey(array, null);

        assertTrue(indexes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testArrayIsCached() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[8];

        List<Integer> indexes = cache.indexesForArrayKey(array, "Trying to cache an array that already exists");
        assertNotNull(indexes);

        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(array);
        assertTrue(cache.arrayIsCached(cachedArray.primaryArray));
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKeyWithNullArray() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();

        try {
            cache.indexesForArrayKey(null, "");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache$CachedArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testCacheArrayWithNullArray() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();

        try {
            cache.cacheArray(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache$CachedArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testArrayIsCachedWithNullKnownArrays() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[4];
        cache.knownArrays = null;

        try {
            cache.arrayIsCached(array);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testIndexesForKeyWithEmptyString() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[2];
        cache.lastArray = array;
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(cache.lastArray);

        List<Integer> indexes = cachedArray.indexesForKey("Ui/_\")");

        assertTrue(indexes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIndexesForKeyWithNonEmptyString() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[8];
        array[1] = "";
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(array);

        List<Integer> indexes = cachedArray.indexesForKey("");

        assertEquals(1, indexes.size());
    }

    @Test(timeout = 4000)
    public void testLastKnownSize() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[8];
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(array);

        int size = cachedArray.lastKnownSize();

        assertEquals(8, size);
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKeyWithEmptyResult() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[1];
        cache.indexesForArrayKey(array, " CZW9XcT");

        List<Integer> indexes = cache.indexesForArrayKey(array, " CZW9XcT");

        assertTrue(indexes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKeyWithNonEmptyResult() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[9];

        List<Integer> indexes1 = cache.indexesForArrayKey(array, array[0]);
        assertEquals(9, indexes1.size());
        assertNotNull(indexes1);

        List<Integer> indexes2 = cache.indexesForArrayKey(array, "");
        assertTrue(indexes2.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCacheArrayThrowsException() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[8];
        cache.cacheArray(array);

        try {
            cache.cacheArray(array);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache", e);
        }
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKeyWithCachedArray() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array1 = new String[1];
        IdentityHashMap<String[], SegmentConstantPoolArrayCache.CachedArray> knownArrays = cache.knownArrays;
        String[] array2 = new String[0];
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(array2);
        knownArrays.put(array1, cachedArray);

        assertEquals(0, cachedArray.lastKnownSize());

        List<Integer> indexes = cache.indexesForArrayKey(array1, array1[0]);
        assertNotNull(indexes);
        assertEquals(1, indexes.size());
    }
}