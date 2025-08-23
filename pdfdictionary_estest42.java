package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link PdfDictionary} class, focusing on the mergeDifferent method.
 */
public class PdfDictionaryTest {

    /**
     * Tests that the mergeDifferent method correctly adds new key-value pairs from a source dictionary
     * into the target dictionary, without overwriting any keys that already exist in the target.
     */
    @Test
    public void mergeDifferent_addsNewKeysWithoutOverwritingExistingOnes() {
        // Arrange: Create a target dictionary and a source dictionary with both
        // overlapping and new keys.
        PdfDictionary targetDictionary = new PdfDictionary();
        targetDictionary.put(new PdfName("ExistingKey"), new PdfString("Original Value"));
        targetDictionary.put(new PdfName("UntouchedKey"), new PdfString("This should not change"));

        PdfDictionary sourceDictionary = new PdfDictionary();
        sourceDictionary.put(new PdfName("ExistingKey"), new PdfString("New Value - Should be ignored"));
        sourceDictionary.put(new PdfName("NewKey"), new PdfString("This should be added"));

        // Act: Merge the source dictionary into the target dictionary.
        targetDictionary.mergeDifferent(sourceDictionary);

        // Assert: Verify that the target dictionary was modified as expected.
        // 1. The final size should include the newly added key.
        assertEquals("Dictionary size should be 3 after merging one new key.", 3, targetDictionary.size());

        // 2. The value for the overlapping key should not have been changed.
        assertEquals("Value of an existing key should not be overwritten.",
                new PdfString("Original Value"), targetDictionary.get(new PdfName("ExistingKey")));

        // 3. The new key from the source dictionary should now exist in the target.
        assertEquals("New key from the source dictionary should be added.",
                new PdfString("This should be added"), targetDictionary.get(new PdfName("NewKey")));

        // 4. The key that was not part of the merge should remain untouched.
        assertEquals("Untouched key should remain in the dictionary.",
                new PdfString("This should not change"), targetDictionary.get(new PdfName("UntouchedKey")));
    }
}