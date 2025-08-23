package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the isFont() method returns false for a newly created, empty dictionary.
     * An empty dictionary does not have its /Type key set to /Font, so it should not be
     * identified as a font dictionary.
     */
    @Test
    public void isFont_shouldReturnFalse_forEmptyDictionary() {
        // Arrange: Create a new, empty PdfDictionary.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Check if the dictionary is of type Font.
        boolean isFont = dictionary.isFont();

        // Assert: The result should be false.
        assertFalse("An empty dictionary should not be identified as a font dictionary", isFont);
    }
}