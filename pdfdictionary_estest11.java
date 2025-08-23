package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that an object added to a PdfDictionary using the putEx() method
     * can be successfully retrieved using the get() method.
     */
    @Test
    public void get_afterPutEx_shouldReturnTheAddedObject() {
        // Arrange: Create a dictionary and a key-value pair to be added.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName testKey = PdfName.ENCODING;
        PdfName testValue = PdfName.MAC_ROMAN_ENCODING;

        // Act: Add the key-value pair to the dictionary and then retrieve the value.
        dictionary.putEx(testKey, testValue);
        PdfObject retrievedValue = dictionary.get(testKey);

        // Assert: Verify that the retrieved object is the same one that was added.
        assertNotNull("The retrieved value should not be null.", retrievedValue);
        assertFalse("The retrieved value should not be a PdfNull object.", retrievedValue.isNull());
        assertEquals("The retrieved value should be identical to the original value.", testValue, retrievedValue);
    }
}