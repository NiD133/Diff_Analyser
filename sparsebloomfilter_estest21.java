package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for {@link SparseBloomFilter} focusing on edge cases with extremely large shapes.
 */
public class SparseBloomFilterLargeShapeTest {

    /**
     * Tests that attempting to merge a SparseBloomFilter with a shape near the maximum
     * integer size throws an OutOfMemoryError.
     *
     * <p>The merge operation, when using a BitMapExtractor, may require creating an
     * internal bitmap representation of the filter. For a filter with a number of bits
     * approaching {@code Integer.MAX_VALUE}, the memory required for this bitmap
     * (approx. 256MB) is expected to exceed the typical heap size allocated for a test
     * environment, leading to an {@code OutOfMemoryError}.</p>
     */
    @Test
    public void mergeWithBitMapExtractorUsingHugeShapeShouldThrowOutOfMemoryError() {
        // Arrange: Create a shape with a number of bits and hash functions close to Integer.MAX_VALUE.
        // This value (2,147,483,639) is chosen to be large enough to trigger memory issues.
        final int nearIntegerMaxValue = 2_147_483_639;
        Shape hugeShape = Shape.fromKM(nearIntegerMaxValue, nearIntegerMaxValue);
        SparseBloomFilter filter = new SparseBloomFilter(hugeShape);

        // Act & Assert: Attempt the merge and verify that it throws an OutOfMemoryError.
        try {
            // The merge operation requires processing the filter's bitmap, which is too large.
            filter.merge((BitMapExtractor) filter);
            fail("Expected an OutOfMemoryError, but no exception was thrown.");
        } catch (final OutOfMemoryError expected) {
            // This is the expected outcome. The test passes.
        }
    }
}