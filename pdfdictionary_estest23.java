package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit test for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsName() returns null when the dictionary value
     * associated with the key is not a PdfName object.
     */
    @Test
    public void getAsNameShouldReturnNullWhenValueIsNotAPdfName() {
        // Arrange: Create a dictionary and add a key with a value that is another dictionary,
        // not a PdfName.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName key = PdfName.TYPE3;
        PdfDictionary valueThatIsNotAPdfName = new PdfDictionary(PdfName.PAGES);
        dictionary.put(key, valueThatIsNotAPdfName);

        // Act: Attempt to retrieve the value as a PdfName.
        PdfName result = dictionary.getAsName(key);

        // Assert: The result must be null because the stored value's type (PdfDictionary)
        // does not match the requested type (PdfName).
        assertNull("getAsName() should return null for a value that is not a PdfName.", result);
    }
}