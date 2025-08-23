package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the contains() method returns false when checking for a null key.
     * A dictionary should not contain a null key, so this check should consistently fail.
     */
    @Test
    public void contains_shouldReturnFalse_forNullKey() {
        // Arrange: Create an empty dictionary instance.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Check if the dictionary contains a null key.
        boolean result = dictionary.contains(null);

        // Assert: The dictionary should report that it does not contain the null key.
        assertFalse("A dictionary should not contain a null key.", result);
    }
}