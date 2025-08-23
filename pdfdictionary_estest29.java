package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Tests that getAsIndirectObject() returns null when the specified key
     * is not present in the dictionary.
     */
    @Test
    public void getAsIndirectObjectShouldReturnNullWhenKeyIsAbsent() {
        // Arrange: Create an empty dictionary that does not contain the key.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName absentKey = PdfName.PAGE;

        // Act: Attempt to retrieve an indirect object for the absent key.
        PdfIndirectReference result = dictionary.getAsIndirectObject(absentKey);

        // Assert: The method should return null as the key does not exist.
        assertNull("Expected getAsIndirectObject() to return null for an absent key.", result);
    }
}