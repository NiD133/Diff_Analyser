package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Tests that {@link PdfDictionary#getDirectObject(PdfName)} returns the original
     * object instance when the stored value is a direct object (i.e., not a
     * {@link PdfIndirectReference}).
     */
    @Test
    public void getDirectObject_shouldReturnSameInstance_forDirectObjectValue() {
        // Arrange: Create a dictionary and add a key-value pair where the value
        // is a direct PdfObject (in this case, a PdfName).
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = PdfName.UF;
        PdfName value = new PdfName("AcroForm");

        dictionary.put(key, value);

        // Act: Retrieve the object using the method under test.
        PdfObject retrievedObject = dictionary.getDirectObject(key);

        // Assert: The retrieved object should be the exact same instance as the
        // value that was put into the dictionary, because no indirect reference
        // resolution was needed.
        assertSame("Expected the exact same object instance for a direct value.", value, retrievedObject);
    }
}