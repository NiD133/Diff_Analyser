package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * This test suite is for the inner class {@link SegmentConstantPoolArrayCache.CachedArray}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Tests that CachedArray.indexesForKey() returns an empty list
     * when the requested key does not exist in the cached array.
     */
    @Test
    public void indexesForKeyShouldReturnEmptyListForNonExistentKey() {
        // Arrange
        // The parent class is needed to instantiate the inner CachedArray class.
        SegmentConstantPoolArrayCache parentCache = new SegmentConstantPoolArrayCache();
        
        // The source array does not contain the key we will search for.
        String[] sourceArray = {"alpha", "beta", "gamma"};
        String nonExistentKey = "delta";

        // Create the CachedArray, which builds an internal index of the source array upon creation.
        SegmentConstantPoolArrayCache.CachedArray cachedArray = parentCache.new CachedArray(sourceArray);

        // Act
        // Search for a key that is not present in the source array.
        List<Integer> foundIndexes = cachedArray.indexesForKey(nonExistentKey);

        // Assert
        // The result should be an empty list, as the key was not found.
        assertTrue("Expected an empty list for a non-existent key, but was not.", foundIndexes.isEmpty());
    }
}