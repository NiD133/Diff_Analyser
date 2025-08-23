package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link PdfDictionary#merge(PdfDictionary)} method.
 */
public class PdfDictionaryMergeTest {

    /**
     * Tests that the merge() method correctly copies all key-value pairs from a source
     * dictionary into the target dictionary.
     */
    @Test
    public void merge_withAnotherDictionary_shouldCopyAllEntries() {
        // Arrange: Create a target dictionary and a source dictionary to merge from.
        // A PdfAction is a valid subclass of PdfDictionary, making it a suitable source.
        PdfDictionary targetDictionary = new PdfDictionary();
        PdfAction sourceActionDictionary = PdfAction.createHide(new Object[0], true);

        // Pre-condition check: Ensure the target is empty and the source is not.
        assertTrue("Target dictionary should be empty before the merge", targetDictionary.getKeys().isEmpty());
        assertFalse("Source dictionary for merging should not be empty", sourceActionDictionary.getKeys().isEmpty());
        int expectedSizeAfterMerge = sourceActionDictionary.size();

        // Act: Perform the merge operation.
        targetDictionary.merge(sourceActionDictionary);

        // Assert: Verify that the target dictionary now contains all entries from the source.
        assertEquals("Target dictionary size should match the source's size after merging",
                expectedSizeAfterMerge, targetDictionary.size());

        // Verify that a few key entries from the source dictionary now exist in the target.
        // A "Hide" action dictionary is expected to contain keys like /S, /T, and /H.
        assertEquals("The value for key 'S' should be copied from the source",
                sourceActionDictionary.get(PdfName.S), targetDictionary.get(PdfName.S));
        assertEquals("The value for key 'T' should be copied from the source",
                sourceActionDictionary.get(PdfName.T), targetDictionary.get(PdfName.T));
        assertEquals("The value for key 'H' should be copied from the source",
                sourceActionDictionary.get(PdfName.H), targetDictionary.get(PdfName.H));
    }
}