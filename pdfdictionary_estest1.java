package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class verifies the behavior of the {@link PdfDictionary} class.
 *
 * The original test was auto-generated, featuring a non-descriptive name (test00)
 * and an assertion that did not clearly relate to the action being tested. This
 * revised version improves clarity and focuses on the specific behavior under test.
 */
public class PdfDictionary_ESTestTest1 {

    /**
     * Verifies that calling the remove() method with a null key on an empty
     * PdfDictionary does not throw an exception and leaves the dictionary unchanged.
     * The underlying HashMap implementation supports null keys, so this operation
     * should be handled gracefully.
     */
    @Test
    public void remove_withNullKeyOnEmptyDictionary_shouldBeIgnored() {
        // Arrange: Create an empty PdfDictionary.
        PdfDictionary dictionary = new PdfDictionary();
        assertEquals("Precondition failed: Dictionary should be empty initially.", 0, dictionary.size());

        // Act: Attempt to remove an entry with a null key.
        // This action should not throw a NullPointerException or any other exception.
        dictionary.remove(null);

        // Assert: The dictionary should remain empty.
        assertEquals("Dictionary should still be empty after attempting to remove a null key.", 0, dictionary.size());
    }
}