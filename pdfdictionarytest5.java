package com.itextpdf.text.pdf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
class PdfDictionaryTest {

    @Test
    void putEx_shouldThrowIllegalArgumentException_whenKeyIsNull() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        PdfObject anyValue = new PdfName("anyValue");
        String expectedMessage = "key is null.";

        // Act
        // The assertThrows method cleanly captures the expected exception.
        IllegalArgumentException thrownException = assertThrows(
            IllegalArgumentException.class,
            () -> dictionary.putEx(null, anyValue)
        );

        // Assert
        // We can now assert details about the captured exception.
        assertEquals(expectedMessage, thrownException.getMessage());
    }
}