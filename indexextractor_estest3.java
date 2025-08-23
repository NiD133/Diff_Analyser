package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.fail; // Keep for the alternative, but not needed for the primary solution

import org.junit.Test;

/**
 * Tests for {@link IndexExtractor}.
 * This class focuses on scenarios related to the uniqueIndices() method.
 */
// The original class name 'IndexExtractor_ESTestTest3' is kept for consistency,
// but in a real-world scenario, it would be renamed to something like 'IndexExtractorTest'.
public class IndexExtractor_ESTestTest3 {

    /**
     * Tests that calling uniqueIndices() on an extractor created from a null array
     * throws a NullPointerException. The factory method fromIndexArray() may accept null,
     * but any subsequent processing operation on the extractor is expected to fail.
     */
    @Test(expected = NullPointerException.class)
    public void uniqueIndicesShouldThrowNullPointerExceptionWhenCreatedFromNullArray() {
        // Arrange: Create an IndexExtractor from a null integer array.
        // The creation itself is not expected to throw an exception.
        IndexExtractor extractor = IndexExtractor.fromIndexArray(null);

        // Act: Attempt to get the unique indices. This should trigger an operation
        // on the null array, resulting in a NullPointerException.
        extractor.uniqueIndices();

        // Assert: The @Test(expected) annotation handles the assertion.
        // If no exception is thrown, the test will fail automatically.
    }
}