package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for IndexExtractor functionality, specifically
 * its interaction with BloomFilter implementations.
 * Note: The original class name and scaffolding suggest this test was auto-generated.
 * It has been refactored for clarity.
 */
public class IndexExtractor_ESTestTest13 extends IndexExtractor_ESTest_scaffolding {

    /**
     * Tests that an empty SparseBloomFilter's 'contains' method correctly returns false
     * when checked against an IndexExtractor that is not empty.
     */
    @Test
    public void testContainsReturnsFalseForEmptyFilterAndNonEmptyExtractor() {
        // Arrange
        // 1. Create an IndexExtractor that contains indices.
        // A bitmap with a single long set to -1L represents 64 enabled bits,
        // which translates to 64 indices (0-63).
        long[] bitmapWithIndices = {-1L};
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitmapWithIndices);
        IndexExtractor nonEmptyExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);

        // 2. Create an empty Bloom filter. The shape must be large enough to
        // theoretically hold the indices from the extractor.
        Shape shape = Shape.fromNM(10, 100); // 10 items, 100 bits
        BloomFilter emptyFilter = new SparseBloomFilter(shape);

        // Act
        // 3. Check if the empty filter contains the indices from the non-empty extractor.
        boolean isContained = emptyFilter.contains(nonEmptyExtractor);

        // Assert
        // 4. The result must be false.
        assertFalse("An empty filter should not contain any indices", isContained);
    }
}