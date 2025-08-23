package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Contains tests for the {@link IndexExtractor} interface, specifically focusing on
 * instances created via the {@code fromIndexArray} static factory method.
 */
public class IndexExtractorTest {

    /**
     * Tests that calling {@code asIndexArray()} on an extractor created with a null array
     * throws a NullPointerException. This verifies that the factory method lazily
     * handles the null input, with the failure occurring upon data access.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenAccessingIndicesFromNullArray() {
        // Arrange: Create an IndexExtractor from a null integer array.
        // The factory method itself should not throw an exception.
        final IndexExtractor extractor = IndexExtractor.fromIndexArray((int[]) null);

        // Act: Attempt to retrieve the data as an array.
        // This action is expected to throw the exception.
        extractor.asIndexArray();

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}