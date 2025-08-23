package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for {@link SimpleBloomFilter}.
 * This class contains tests for the merge functionality, specifically focusing on error handling.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that attempting to merge two Bloom filters with incompatible shapes
     * throws an IllegalArgumentException.
     *
     * <p>Two shapes are considered incompatible if they do not have the same number of
     * hash functions (k) and the same number of bits (m). This test verifies the
     * case where the number of bits differs.</p>
     */
    @Test(timeout = 4000)
    public void mergeWithIncompatibleShapeThrowsIllegalArgumentException() {
        // Arrange: Create two Bloom filters with shapes that have a different number of bits.
        Shape targetShape = Shape.fromKM(10, 1024); // k=10, m=1024
        Shape incompatibleShape = Shape.fromKM(10, 2048); // Same k, but different m

        SimpleBloomFilter targetFilter = new SimpleBloomFilter(targetShape);
        SimpleBloomFilter sourceFilter = new SimpleBloomFilter(incompatibleShape);

        // Act & Assert: Expect an IllegalArgumentException when merging.
        try {
            targetFilter.merge(sourceFilter);
            fail("Expected an IllegalArgumentException because the filter shapes are incompatible.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message clearly states the incompatibility.
            final String expectedMessage = String.format("Shape %s is not compatible with %s",
                incompatibleShape, targetShape);
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}