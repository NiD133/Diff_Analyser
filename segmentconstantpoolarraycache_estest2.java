package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the inner class {@link SegmentConstantPoolArrayCache.CachedArray}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Tests that a new CachedArray instance correctly records the size
     * of the source array it was created with.
     */
    @Test
    public void cachedArrayShouldStoreInitialSizeOfSourceArray() {
        // Arrange
        SegmentConstantPoolArrayCache cacheManager = new SegmentConstantPoolArrayCache();
        final int expectedSize = 8;
        String[] sourceArray = new String[expectedSize];

        // Act
        // The constructor of CachedArray is the action being tested, as it is responsible
        // for initializing the internal state, including the size.
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cacheManager.new CachedArray(sourceArray);

        // Assert
        assertEquals("The last known size should match the source array's length after construction.",
            expectedSize, cachedArray.lastKnownSize());
    }
}