package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.IdentityHashMap;

/**
 * Unit tests for {@link SegmentConstantPoolArrayCache}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Tests that arrayIsCached() returns false if a cache entry exists for an array
     * but its stored size does not match the actual size of the array being checked.
     * This simulates a scenario where an array was modified (e.g., grew) after
     * being cached, which should invalidate the cache entry.
     */
    @Test
    public void arrayIsCachedShouldReturnFalseWhenCachedSizeMismatches() {
        // --- Arrange ---
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();

        // The array we will query the cache for. It has a size of 4.
        String[] arrayToQuery = new String[4];

        // A different array with a different size (5). We will use this to create
        // an outdated cache entry.
        String[] sourceOfOutdatedCache = new String[5];

        // This is a white-box test. We manually create an inconsistent state
        // to test the method's robustness. We create a cache entry from one array...
        SegmentConstantPoolArrayCache.CachedArray outdatedCachedArray = cache.new CachedArray(sourceOfOutdatedCache);

        // ...and then manually insert it into the cache's internal map using a different
        // array instance as the key.
        IdentityHashMap<String[], SegmentConstantPoolArrayCache.CachedArray> knownArraysMap = new IdentityHashMap<>();
        knownArraysMap.put(arrayToQuery, outdatedCachedArray);
        cache.knownArrays = knownArraysMap;

        // Sanity check: The cached object correctly reports the size of the array it was created from.
        assertEquals(5, outdatedCachedArray.lastKnownSize());

        // --- Act ---
        // Check if the original array (size 4) is considered cached. The implementation
        // should find the entry but then detect that arrayToQuery.length (4)
        // does not equal the cached size (5).
        boolean isCached = cache.arrayIsCached(arrayToQuery);

        // --- Assert ---
        assertFalse("Cache should be considered invalid due to size mismatch", isCached);
    }
}