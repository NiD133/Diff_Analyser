package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test class verifies the behavior of the PdfDictionary class.
 */
public class PdfDictionaryRefactoredTest {

    /**
     * Tests that getAsBoolean() returns null when the requested key does not exist in the dictionary.
     */
    @Test
    public void getAsBoolean_shouldReturnNull_whenKeyIsMissing() {
        // Arrange: Create an empty dictionary.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName nonExistentKey = PdfDictionary.PAGES; // A standard key that is not in an empty dictionary.

        // Act: Attempt to retrieve a PdfBoolean for a key that is not present.
        PdfBoolean result = dictionary.getAsBoolean(nonExistentKey);

        // Assert: The method should return null, indicating the key was not found.
        assertNull("Expected getAsBoolean to return null for a non-existent key.", result);
    }
}