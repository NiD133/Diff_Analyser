package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that merging a hasher that produces an index outside the valid range
     * for the filter's shape throws an IllegalArgumentException.
     */
    @Test
    public void testMergeHasherWithOutOfBoundsIndexThrowsIllegalArgumentException() {
        // Arrange
        // Create a shape with only one bit. This means the only valid index is 0.
        final int numBits = 1;
        final int numHashFunctions = 3130; // This value is not critical, but matches the original test.
        final Shape shape = Shape.fromKM(numHashFunctions, numBits);
        final SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Create a hasher that is known to produce indices. The test relies on this
        // hasher producing at least one index greater than 0, which is out of bounds
        // for our shape.
        final Hasher hasher = new EnhancedDoubleHasher(3130, 3130);

        // Act & Assert
        try {
            filter.merge(hasher);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException expected) {
            // The maximum allowed index is numBits - 1.
            final int maxAllowedIndex = numBits - 1;
            final String expectedMessagePart = String.format("is greater than maximum value (%d)", maxAllowedIndex);

            // Verify the exception message confirms the index was out of bounds.
            assertTrue(
                String.format("Exception message should contain '%s'. Actual: %s",
                    expectedMessagePart, expected.getMessage()),
                expected.getMessage().contains(expectedMessagePart)
            );
        }
    }
}