package com.itextpdf.text.pdf;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the getKeys() method on a newly created PdfDictionary
     * returns an empty set.
     */
    @Test
    public void getKeys_onNewDictionary_shouldReturnEmptySet() {
        // Arrange: Create a new, empty dictionary.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Retrieve the set of keys.
        Set<PdfName> keys = dictionary.getKeys();

        // Assert: The returned set should be empty.
        assertTrue("A new PdfDictionary should not contain any keys.", keys.isEmpty());
    }
}