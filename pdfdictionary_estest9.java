package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.collection.PdfCollectionField;
import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on verifying the behavior of the PdfDictionary class.
 */
public class PdfDictionary_ESTestTest9 {

    /**
     * Verifies that the get() method returns the exact same object instance
     * that was previously added using the put() method.
     */
    @Test
    public void get_shouldReturnSameObjectThatWasPut() {
        // Arrange: Create a dictionary and the key-value pair to be stored.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName testKey = new PdfName("TestKey");
        // PdfCollectionField is a valid PdfObject, suitable for use as a value.
        PdfObject expectedValue = new PdfCollectionField("TestValue", 0);

        // Act: Put the value into the dictionary and then retrieve it.
        dictionary.put(testKey, expectedValue);
        PdfObject actualValue = dictionary.get(testKey);

        // Assert: Verify that the retrieved object is the same instance as the original.
        assertSame("The dictionary should return the exact same object instance that was put into it.",
                expectedValue, actualValue);
    }
}