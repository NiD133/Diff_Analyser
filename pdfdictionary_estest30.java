package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsBoolean() returns null when the value associated with a key
     * is not a PdfBoolean object.
     */
    @Test
    public void getAsBooleanShouldReturnNullWhenEntryIsNotABoolean() {
        // Arrange: Create a dictionary and add an entry where the value is a PdfName,
        // which is not a PdfBoolean.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = PdfName.URL;
        PdfName nonBooleanValue = new PdfName("SomeValue");
        dictionary.put(key, nonBooleanValue);

        // Act: Attempt to retrieve the entry as a PdfBoolean.
        PdfBoolean result = dictionary.getAsBoolean(key);

        // Assert: The result must be null, as the stored value cannot be cast to PdfBoolean.
        assertNull("getAsBoolean() should return null for a non-boolean value.", result);
    }
}