package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test class contains tests for the PdfDictionary class.
 * The original class name "PdfDictionary_ESTestTest28" was kept to match the original file,
 * but a more descriptive name like "PdfDictionaryTest" would be preferable in a real-world scenario.
 */
public class PdfDictionary_ESTestTest28 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Verifies that getAsIndirectObject() returns null when the value associated
     * with a key is a direct object, not a PdfIndirectReference.
     */
    @Test
    public void getAsIndirectObject_whenValueIsNotAnIndirectReference_shouldReturnNull() {
        // Arrange: Create a dictionary and add a key-value pair where the value
        // is a direct PdfObject (a PdfName), not an indirect reference.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = new PdfName("TestKey");
        PdfName value = new PdfName("TestValue");
        dictionary.put(key, value);

        // Act: Attempt to retrieve the value as an indirect reference.
        PdfIndirectReference result = dictionary.getAsIndirectObject(key);

        // Assert: The result should be null, as the stored value was not an
        // instance of PdfIndirectReference.
        assertNull("Expected getAsIndirectObject() to return null for a non-indirect reference value.", result);
    }
}