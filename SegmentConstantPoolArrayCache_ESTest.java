package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SegmentConstantPoolArrayCacheTest {

    // Caches on-demand when queried and returns correct indices
    @Test
    public void indexesForArrayKey_cachesArrayOnFirstUse_andReturnsIndices() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[] {"a", "b", "a", null};

        // not cached yet
        assertFalse(cache.arrayIsCached(array));

        // query triggers caching and returns positions of "a"
        List<Integer> indices = cache.indexesForArrayKey(array, "a");
        assertEquals(Arrays.asList(0, 2), indices);

        // now the array is cached
        assertTrue(cache.arrayIsCached(array));
    }

    // Null key returns indices of all null elements in the array
    @Test
    public void indexesForArrayKey_handlesNullKey_byReturningIndicesOfNulls() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[] {null, "x", "y", null, null};

        List<Integer> indices = cache.indexesForArrayKey(array, null);
        assertEquals(Arrays.asList(0, 3, 4), indices);
    }

    // Unknown key yields an empty result
    @Test
    public void indexesForArrayKey_returnsEmptyList_forMissingKey() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[] {"foo", "bar", "baz"};

        List<Integer> indices = cache.indexesForArrayKey(array, "missing");
        assertTrue(indices.isEmpty());
    }

    // Explicitly caching the same array twice is not allowed
    @Test(expected = IllegalArgumentException.class)
    public void cacheArray_whenCalledTwiceWithSameArray_throwsIllegalArgumentException() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[] {"x", "y"};

        cache.cacheArray(array);
        cache.cacheArray(array); // should throw
    }

    // Null array inputs are rejected
    @Test(expected = NullPointerException.class)
    public void indexesForArrayKey_throwsNullPointerException_whenArrayIsNull() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        cache.indexesForArrayKey(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void cacheArray_throwsNullPointerException_whenArrayIsNull() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        cache.cacheArray(null);
    }

    // Basic behavior of the inner CachedArray helper
    @Test
    public void cachedArray_reportsLastKnownSize_andIndexesForKey() {
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] array = new String[] {"", "", null};
        SegmentConstantPoolArrayCache.CachedArray cached = cache.new CachedArray(array);

        // size is known at construction time
        assertEquals(3, cached.lastKnownSize());

        // re-caching indexes should not change the reported size
        cached.cacheIndexes();
        assertEquals(3, cached.lastKnownSize());

        // lookups
        assertEquals(Arrays.asList(0, 1), cached.indexesForKey(""));
        assertEquals(Arrays.asList(2), cached.indexesForKey(null));
        assertTrue(cached.indexesForKey("absent").isEmpty());
    }
}