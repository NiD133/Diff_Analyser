package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the checkType() method returns false when the provided type is null.
     *
     * A PDF dictionary's type cannot be null, so the method should correctly
     * identify that a null input does not match the dictionary's type.
     */
    @Test
    public void checkType_shouldReturnFalse_whenTypeIsNull() {
        // Arrange: Create an instance of a PdfDictionary subclass.
        // PdfResources is used here as a concrete implementation for the test.
        PdfDictionary dictionary = new PdfResources();

        // Act: Call the method under test with a null argument.
        boolean result = dictionary.checkType(null);

        // Assert: Verify that the method returned false.
        assertFalse("checkType(null) should return false.", result);
    }
}