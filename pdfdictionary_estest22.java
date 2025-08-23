package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsName() returns null when the requested key is not present in the dictionary.
     */
    @Test
    public void getAsNameShouldReturnNullWhenKeyDoesNotExist() {
        // Arrange: Create an empty dictionary and define a key that is known not to be present.
        // The method under test, getAsName(), is part of PdfDictionary, so we test it directly
        // instead of through a subclass like the original test did.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName nonExistentKey = PdfName.CATALOG;

        // Act: Attempt to retrieve a PdfName using the non-existent key.
        PdfName result = dictionary.getAsName(nonExistentKey);

        // Assert: The result should be null, as the key was not found in the empty dictionary.
        assertNull("Expected getAsName() to return null for a non-existent key.", result);
    }
}