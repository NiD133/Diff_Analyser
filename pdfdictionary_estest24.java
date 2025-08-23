package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the checkType() method returns false when the dictionary's
     * actual type does not match the type being checked against.
     */
    @Test
    public void checkTypeShouldReturnFalseForMismatchedType() {
        // Arrange: Create a dictionary with a specific type, for example, /Font.
        PdfDictionary dictionary = new PdfDictionary(PdfName.FONT);
        PdfName typeToCompare = PdfName.CATALOG;

        // Act: Check if the dictionary's type is /Catalog.
        boolean isTypeMatch = dictionary.checkType(typeToCompare);

        // Assert: The result should be false, as the dictionary's type is /Font, not /Catalog.
        assertFalse("checkType() should return false when types do not match.", isTypeMatch);
    }
}