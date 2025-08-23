package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test suite contains tests for the PdfDictionary class.
 * This specific test focuses on the behavior of the getAsIndirectObject method.
 */
public class PdfDictionary_ESTestTest38 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Verifies that getAsIndirectObject() returns null when the value
     * associated with a key is not a PdfIndirectReference.
     */
    @Test(timeout = 4000)
    public void getAsIndirectObject_whenValueIsNotAnIndirectReference_shouldReturnNull() {
        // Arrange: Create a dictionary and add a key-value pair where the value
        // is a PdfName, which is not a PdfIndirectReference.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName testKey = new PdfName("MyKey");
        PdfName nonReferenceValue = new PdfName("MyValue");
        dictionary.put(testKey, nonReferenceValue);

        // Act: Attempt to retrieve the value as a PdfIndirectReference.
        PdfIndirectReference result = dictionary.getAsIndirectObject(testKey);

        // Assert: The result should be null, as the stored value cannot be cast
        // to a PdfIndirectReference.
        assertNull("Expected getAsIndirectObject to return null for a non-reference value.", result);
    }
}