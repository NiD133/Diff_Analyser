package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * A focused test suite for the contains() method of the {@link PdfDictionary} class.
 */
public class PdfDictionaryContainsTest {

    /**
     * Verifies that the contains() method returns true for a key
     * that has been previously added to the dictionary.
     */
    @Test
    public void containsShouldReturnTrueWhenKeyIsPresent() {
        // Arrange: Create a new dictionary and add a key-value pair.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = PdfName.TYPE3;
        dictionary.putEx(key, key); // The value can be any PdfObject.

        // Act & Assert: Verify that the dictionary reports containing the added key.
        assertTrue(
            "The dictionary should report 'true' for a key that was just added.",
            dictionary.contains(key)
        );
    }
}