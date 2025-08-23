package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link PdfDictionary} class, focusing on its type-safe getter methods.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsStream() returns null when the dictionary entry for the given key
     * exists but is not a PdfStream object.
     */
    @Test
    public void getAsStreamShouldReturnNullWhenValueIsNotAStream() {
        // Arrange: Create a dictionary and add a key with a non-stream value.
        // In this case, the value is a PdfName, which is a valid but incorrect type.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName testKey = PdfName.DR;
        PdfName nonStreamValue = new PdfName("SomeValue");
        dictionary.put(testKey, nonStreamValue);

        // Act: Attempt to retrieve the value as a PdfStream.
        PdfStream result = dictionary.getAsStream(testKey);

        // Assert: The result should be null, as the stored value is not a stream.
        assertNull("getAsStream() should return null for a non-stream value.", result);
    }
}