package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for the {@link IndexExtractor} interface, particularly its interaction
 * with other components like {@link BloomFilter}.
 */
public class IndexExtractorTest {

    /**
     * Tests that an IndexExtractor, created from duplicate indices and then made unique,
     * is correctly evaluated by a BloomFilter's 'contains' method.
     *
     * This test verifies that an empty BloomFilter correctly reports that it does not
     * contain an index provided by the extractor.
     */
    @Test
    public void testUniqueIndicesExtractorIsNotFoundInEmptyFilter() {
        // Arrange
        // 1. Create an IndexExtractor from an array containing duplicate indices.
        int[] indicesWithDuplicates = {0, 0, 0, 0};
        IndexExtractor extractorWithDuplicates = IndexExtractor.fromIndexArray(indicesWithDuplicates);

        // 2. Get a new extractor that provides only the unique indices (in this case, just '0').
        IndexExtractor uniqueIndicesExtractor = extractorWithDuplicates.uniqueIndices();

        // 3. Create an empty BloomFilter with a standard shape.
        Shape shape = Shape.fromKM(17, 72); // A typical shape for a Bloom filter.
        BloomFilter emptyFilter = new SimpleBloomFilter(shape);

        // Act
        // 4. Check if the empty filter contains the indices from the unique extractor.
        boolean contains = emptyFilter.contains(uniqueIndicesExtractor);

        // Assert
        // 5. An empty filter should not contain any indices, so the result must be false.
        assertFalse("An empty filter should not report containing any indices", contains);
    }
}