package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsDict() returns null when the value associated with a key
     * is not a PdfDictionary.
     */
    @Test
    public void getAsDict_whenValueIsNotADictionary_shouldReturnNull() {
        // Arrange: Create a dictionary and add a key-value pair where the value is a PdfName,
        // not a PdfDictionary.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = PdfName.DR;
        PdfObject nonDictionaryValue = new PdfName("SomeValue");
        dictionary.put(key, nonDictionaryValue);

        // Act: Attempt to retrieve the value as a PdfDictionary.
        PdfDictionary result = dictionary.getAsDict(key);

        // Assert: The result should be null, as the stored value is not of the requested type.
        assertNull("Expected getAsDict() to return null for a non-dictionary value.", result);
    }
}