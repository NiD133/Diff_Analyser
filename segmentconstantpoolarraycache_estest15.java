package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * This test verifies that {@code indexesForArrayKey} correctly handles a stale cache entry.
     * A cache entry is considered "stale" if the array it's associated with has changed
     * (in this case, detected by a size mismatch) since it was last cached. The method
     * should detect this, rebuild the cache for the array, and then return the correct indexes.
     */
    @Test
    public void indexesForArrayKeyShouldUpdateStaleCacheAndReturnCorrectIndexes() {
        // Arrange: Create a cache and manually insert a stale entry.
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();

        // The array we intend to search. It contains a single null element.
        String[] arrayToSearch = { null };

        // A different, empty array used to create the stale cache data.
        String[] staleCacheSourceArray = new String[0];

        // Create a CachedArray instance based on the empty array. Its last known size will be 0.
        SegmentConstantPoolArrayCache.CachedArray staleCachedArray =
            cache.new CachedArray(staleCacheSourceArray);

        // Manually insert the stale entry into the cache's internal map.
        // We map `arrayToSearch` to the cache data from `staleCacheSourceArray`.
        // The cache should detect this mismatch because arrayToSearch.length (1)
        // is not equal to staleCachedArray.lastKnownSize() (0).
        cache.knownArrays.put(arrayToSearch, staleCachedArray);

        // Act: Call the method under test. It should detect the stale cache,
        // rebuild it for `arrayToSearch`, and then perform the search for the null key.
        List<Integer> foundIndexes = cache.indexesForArrayKey(arrayToSearch, null);

        // Assert: Verify that the correct index was found after the cache was rebuilt.
        // The value `null` is at index 0 in `arrayToSearch`.
        assertNotNull("The returned list of indexes should not be null.", foundIndexes);
        assertEquals("Should find exactly one occurrence of the key.", 1, foundIndexes.size());
        assertEquals("The index of the key should be 0.", Integer.valueOf(0), foundIndexes.get(0));
    }
}