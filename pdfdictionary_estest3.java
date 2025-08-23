package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the PdfDictionary class.
 * This specific test verifies the behavior of the putAll() method.
 */
public class PdfDictionary_ESTestTest3 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Tests that putAll() correctly copies all key-value pairs from a source
     * dictionary to a target dictionary, overwriting any existing keys.
     */
    @Test
    public void putAll_shouldCopyEntriesFromSourceAndOverwriteExistingKeys() {
        // Arrange
        // Create a source dictionary with a unique entry and one that will conflict with the target.
        PdfDictionary sourceDictionary = new PdfDictionary();
        sourceDictionary.put(PdfName.CONTENTS, new PdfString("Source Content"));
        sourceDictionary.put(PdfName.TYPE, PdfName.PAGE); // This key also exists in the target

        // Create a target dictionary with its own entries.
        PdfDictionary targetDictionary = new PdfDictionary();
        targetDictionary.put(PdfName.AUTHOR, new PdfString("Initial Author"));
        targetDictionary.put(PdfName.TYPE, PdfName.CATALOG); // This value will be overwritten

        // Act
        // Perform the operation under test: copy all entries from source to target.
        targetDictionary.putAll(sourceDictionary);

        // Assert
        // Verify the final state of the target dictionary.
        assertEquals("Dictionary size should reflect merged keys.", 3, targetDictionary.size());

        // 1. Verify that the non-conflicting entry from the target dictionary is retained.
        assertEquals("Retained original value for non-conflicting key",
                new PdfString("Initial Author"), targetDictionary.get(PdfName.AUTHOR));

        // 2. Verify that the new entry from the source dictionary was added.
        assertEquals("Added new value from source",
                new PdfString("Source Content"), targetDictionary.get(PdfName.CONTENTS));

        // 3. Verify that the conflicting entry was overwritten with the value from the source dictionary.
        assertEquals("Overwrote existing value for conflicting key",
                PdfName.PAGE, targetDictionary.get(PdfName.TYPE));
    }
}