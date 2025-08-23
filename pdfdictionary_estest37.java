package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsStream() returns null when the requested key does not exist in the dictionary.
     *
     * This test ensures that the type-safe getter gracefully handles cases where a key-value pair
     * is absent, preventing potential errors and returning a predictable null value.
     */
    @Test
    public void getAsStream_whenKeyDoesNotExist_shouldReturnNull() {
        // Arrange: Create an empty dictionary.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Attempt to retrieve a non-existent key as a PdfStream.
        PdfStream result = dictionary.getAsStream(PdfName.PAGES);

        // Assert: The result should be null, as the key is not present in the dictionary.
        assertNull("Expected getAsStream() to return null for a non-existent key.", result);
    }
}