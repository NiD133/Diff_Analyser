package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the clear() method removes all key-value pairs from the dictionary,
     * resulting in an empty dictionary.
     */
    @Test
    public void clear_shouldRemoveAllEntriesFromDictionary() {
        // Arrange: Create a dictionary and add a few entries to it.
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.TYPE, PdfName.PAGE);
        dictionary.put(PdfName.KIDS, new PdfArray());
        
        // Pre-condition check: Ensure the dictionary is not empty before the action.
        assertFalse("The dictionary should contain entries before being cleared.", dictionary.getKeys().isEmpty());

        // Act: Call the method under test.
        dictionary.clear();

        // Assert: Verify that the dictionary is now empty.
        assertTrue("The dictionary should be empty after calling clear().", dictionary.getKeys().isEmpty());
        assertEquals("The dictionary size should be 0 after calling clear().", 0, dictionary.size());
    }
}