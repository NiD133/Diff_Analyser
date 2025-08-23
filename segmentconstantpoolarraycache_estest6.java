package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Verifies that calling indexesForArrayKey() with a null array argument
     * correctly throws a NullPointerException. The cache mechanism cannot
     * operate on a null array.
     */
    @Test(expected = NullPointerException.class)
    public void indexesForArrayKeyShouldThrowNPEForNullArray() {
        // Arrange
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String anyKey = "anyValue"; // The key's value is irrelevant for this test

        // Act & Assert
        // This call is expected to throw a NullPointerException because the input array is null.
        cache.indexesForArrayKey(null, anyKey);
    }
}