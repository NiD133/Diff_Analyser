package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Contains unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the get() method returns null when called with a key
     * that does not exist in the dictionary.
     */
    @Test
    public void get_withNonExistentKey_shouldReturnNull() {
        // Arrange: Create an empty dictionary and a key that is not present.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName nonExistentKey = new PdfName("ThisKeyDoesNotExist");

        // Act: Attempt to retrieve the value for the non-existent key.
        PdfObject result = dictionary.get(nonExistentKey);

        // Assert: The result should be null, as the key was never added.
        assertNull("Expected get() to return null for a non-existent key.", result);
    }
}