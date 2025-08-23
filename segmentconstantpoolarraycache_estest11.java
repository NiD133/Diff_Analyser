package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SegmentConstantPoolArrayCache.CachedArray} inner class.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Verifies that the {@code lastKnownSize()} method of a new {@code CachedArray}
     * instance correctly returns the length of the array it was constructed with.
     */
    @Test
    public void lastKnownSizeShouldReturnArrayLengthOnCreation() {
        // Arrange: Create a parent cache instance and an input array of a specific size.
        final int expectedSize = 8;
        final String[] inputArray = new String[expectedSize];
        final SegmentConstantPoolArrayCache parentCache = new SegmentConstantPoolArrayCache();

        // Act: Create the CachedArray and get its last known size.
        final SegmentConstantPoolArrayCache.CachedArray cachedArray = parentCache.new CachedArray(inputArray);
        final int actualSize = cachedArray.lastKnownSize();

        // Assert: The returned size should match the original array's length.
        assertEquals(expectedSize, actualSize);
    }
}