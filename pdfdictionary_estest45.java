package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that calling putEx() with a null value does not add an entry to the dictionary.
     * <p>
     * According to its documentation, the putEx() method should do nothing if the provided
     * value is null. This test ensures that the dictionary's size remains unchanged and
     * the key is not added.
     */
    @Test
    public void putEx_withNullValue_shouldNotModifyDictionary() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = new PdfName("TestKey");

        // Act
        dictionary.putEx(key, null);

        // Assert
        assertTrue("The dictionary should be empty after calling putEx with a null value.",
                   dictionary.getKeys().isEmpty());
        assertFalse("The dictionary should not contain the key after calling putEx with a null value.",
                    dictionary.contains(key));
    }
}