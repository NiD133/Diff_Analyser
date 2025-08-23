package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link IndexExtractor}.
 */
public class IndexExtractorTest {

    /**
     * Tests that {@code uniqueIndices()} throws an IndexOutOfBoundsException
     * if the extractor was created with a negative index. This aligns with the
     * method's Javadoc contract.
     */
    @Test
    public void uniqueIndicesShouldThrowExceptionWhenCreatedWithNegativeIndex() {
        // Arrange: Create an IndexExtractor from an array containing a negative index.
        int[] indicesWithNegativeValue = {-1};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indicesWithNegativeValue);

        // Act & Assert: Attempt to get unique indices and verify the correct exception is thrown.
        try {
            indexExtractor.uniqueIndices();
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Verify that the exception message is as expected, confirming the cause.
            assertEquals("bitIndex < 0: -1", e.getMessage());
        }
    }
}