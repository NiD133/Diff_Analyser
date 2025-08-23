package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link PdfDictionary} class, focusing on getter methods.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsString() returns null when the requested key is not present in the dictionary.
     */
    @Test
    public void getAsStringShouldReturnNullWhenKeyDoesNotExist() {
        // Arrange: Create an empty dictionary.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName nonExistentKey = PdfName.CATALOG;

        // Act: Attempt to retrieve a PdfString for a key that has not been added.
        PdfString result = dictionary.getAsString(nonExistentKey);

        // Assert: The method should return null, indicating the key was not found.
        assertNull("Expected getAsString() to return null for a non-existent key.", result);
    }
}