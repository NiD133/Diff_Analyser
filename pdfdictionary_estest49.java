package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the toString() method correctly formats the output string
     * to include the dictionary's type when a type has been set.
     */
    @Test
    public void toString_shouldReturnStringWithTypeName_whenDictionaryHasType() {
        // Arrange
        // PdfSigLockDictionary is a concrete subclass of PdfDictionary that sets its
        // type to "/SigFieldLock" in its constructor. This makes it a convenient
        // subject for testing the toString() behavior of a typed dictionary.
        PdfDictionary dictionary = new PdfSigLockDictionary();
        String expectedString = "Dictionary of type: /SigFieldLock";

        // Act
        String actualString = dictionary.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}