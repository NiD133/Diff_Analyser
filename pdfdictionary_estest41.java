package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that attempting to add an entry with a null key
     * to a PdfDictionary throws an IllegalArgumentException.
     */
    @Test
    public void put_withNullKey_throwsIllegalArgumentException() {
        // Arrange: Create a dictionary and a value to be added.
        PdfDictionary dictionary = new PdfDictionary();
        PdfObject someValue = new PdfString("any value");

        // Act & Assert: Attempt the invalid operation and verify the exception.
        try {
            dictionary.put(null, someValue);
            fail("Expected an IllegalArgumentException because the key cannot be null.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and correct.
            assertEquals("key is null.", e.getMessage());
        }
    }
}