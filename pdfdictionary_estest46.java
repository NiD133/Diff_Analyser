package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that calling putEx() with a null key throws an IllegalArgumentException.
     * The PDF specification requires dictionary keys to be non-null PdfName objects.
     */
    @Test
    public void putEx_withNullKey_shouldThrowIllegalArgumentException() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        String expectedErrorMessage = "key is null.";

        // Act & Assert
        try {
            dictionary.putEx(null, null); // The value is irrelevant, the key is being tested.
            fail("Expected an IllegalArgumentException to be thrown, but no exception was caught.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}