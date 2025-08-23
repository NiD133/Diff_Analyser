package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that a newly created PdfDictionary is empty.
     */
    @Test
    public void newDictionary_shouldHaveSizeZero() {
        // Arrange: Create a new, empty PdfDictionary instance.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Get the size of the dictionary.
        int size = dictionary.size();

        // Assert: The size should be 0, indicating the dictionary is empty.
        assertEquals("A newly created dictionary should be empty.", 0, size);
    }
}