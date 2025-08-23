package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that an empty filter "contains" another empty filter (in this case, itself).
     *
     * <p>The {@code contains(IndexExtractor)} method checks if all indices from the provided
     * extractor are present in the filter. When the provided extractor is empty, it
     * contains no indices. Therefore, the condition "all indices are present" is
     * vacuously true, and the method should return {@code true}.</p>
     */
    @Test
    public void testContainsWithEmptyExtractorReturnsTrue() {
        // Arrange
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);

        // Act
        // Check if an empty filter contains the indices from another empty filter (itself).
        boolean result = emptyFilter.contains(emptyFilter);

        // Assert
        assertTrue("An empty filter should be considered to contain another empty filter.", result);
    }
}