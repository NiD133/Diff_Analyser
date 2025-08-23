package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link PdfDictionary} class.
 */
// The original test class name is kept for context, but in a real scenario,
// it would be renamed to something like PdfDictionaryTest.
public class PdfDictionary_ESTestTest47 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Verifies that the put() method correctly adds a new key-value pair
     * to an empty dictionary.
     */
    @Test
    public void put_onEmptyDictionary_shouldAddKeyValuePair() {
        // Arrange: Create an empty dictionary and a key-value pair to add.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = PdfName.TYPE;
        PdfObject value = PdfName.CATALOG; // A typical value for the TYPE key.

        // Act: Add the key-value pair to the dictionary.
        dictionary.put(key, value);

        // Assert: Verify that the dictionary now contains the new entry.
        assertEquals("The dictionary should contain exactly one entry.", 1, dictionary.size());
        assertTrue("The dictionary should report containing the added key.", dictionary.contains(key));
        assertEquals("The value retrieved for the key should match the value that was put.", value, dictionary.get(key));
    }
}