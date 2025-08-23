package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Contains unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsNumber() returns null when the requested key holds a value
     * that is not a PdfNumber. This ensures the type-safe getter handles
     * type mismatches gracefully.
     */
    @Test
    public void getAsNumber_shouldReturnNull_whenValueIsOfIncorrectType() {
        // Arrange: Create a dictionary and add an entry where the value is a PdfName,
        // not the expected PdfNumber.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName testKey = PdfName.FRM;
        PdfName valueWithIncorrectType = new PdfName("NotANumber");
        dictionary.put(testKey, valueWithIncorrectType);

        // Act: Attempt to retrieve the value using the type-specific getAsNumber() method.
        PdfNumber result = dictionary.getAsNumber(testKey);

        // Assert: The method should return null because the stored object cannot be cast to PdfNumber.
        assertNull("Expected getAsNumber() to return null for a non-PdfNumber value.", result);
    }
}