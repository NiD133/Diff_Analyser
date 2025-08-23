package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link IndexExtractor} interface, focusing on default method implementations.
 */
public class IndexExtractorTest {

    /**
     * Tests that uniqueIndices() correctly identifies a single unique index
     * from an input array containing only duplicates of that index.
     */
    @Test
    public void uniqueIndicesShouldReturnSingleValueWhenInputContainsOnlyDuplicates() {
        // Arrange: Create an IndexExtractor from an array with multiple duplicate values.
        // Using an explicit array literal makes the input data clear.
        int[] indicesWithDuplicates = {0, 0, 0, 0};
        IndexExtractor extractorWithDuplicates = IndexExtractor.fromIndexArray(indicesWithDuplicates);

        // Act: Call the method under test to get an extractor for the unique indices.
        IndexExtractor uniqueExtractor = extractorWithDuplicates.uniqueIndices();

        // Assert: Verify that the resulting extractor yields only the single unique index.
        int[] resultIndices = uniqueExtractor.asIndexArray();

        assertEquals("The array of unique indices should contain exactly one element.", 1, resultIndices.length);
        assertArrayEquals("The single unique index should be 0.", new int[]{0}, resultIndices);
    }
}