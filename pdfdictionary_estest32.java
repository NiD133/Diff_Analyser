package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link PdfDictionary} class, focusing on its type-safe getter methods.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsName() correctly retrieves the exact PdfName instance
     * that was previously stored in the dictionary.
     */
    @Test
    public void getAsName_shouldReturnSameInstance_whenValueIsPdfName() {
        // Arrange: Create a dictionary and a PdfName object to store.
        // We use PdfDictionary directly, as the method under test belongs to it.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName expectedName = PdfWriter.PDF_VERSION_1_6;
        dictionary.put(expectedName, expectedName);

        // Act: Attempt to retrieve the stored object as a PdfName.
        PdfName actualName = dictionary.getAsName(expectedName);

        // Assert: The retrieved object must be the same instance as the one stored.
        assertSame("The retrieved object should be the exact same instance as the one that was put into the dictionary.",
                expectedName, actualName);
    }
}