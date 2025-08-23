package com.itextpdf.text.pdf;

import org.junit.Test;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the PdfDictionary class, focusing on its merge operations.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that merging a dictionary with itself using the mergeDifferent() method
     * does not alter the dictionary's contents.
     */
    @Test
    public void mergeDifferent_whenMergingWithSelf_shouldNotChangeDictionary() {
        // Arrange: Create a dictionary with some initial content.
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.TYPE, PdfName.CATALOG);
        dictionary.put(PdfName.VERSION, new PdfString("1.7"));

        // Create a copy of the original state for later comparison.
        // We use the underlying hashMap for a reliable equality check.
        LinkedHashMap<PdfName, PdfObject> expectedState = new LinkedHashMap<>(dictionary.hashMap);

        // Act: Merge the dictionary with itself.
        dictionary.mergeDifferent(dictionary);

        // Assert: The dictionary's content must remain unchanged.
        assertEquals("Merging a dictionary with itself should not alter its contents.",
                     expectedState, dictionary.hashMap);
    }
}