package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Tests for the {@link PdfDictionary} class, focusing on its robustness.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that putEx() throws a NullPointerException if the dictionary's
     * internal map has been unexpectedly set to null.
     *
     * This test ensures the method is robust against an invalid internal state,
     * which could occur due to unforeseen circumstances or incorrect subclassing.
     */
    @Test(expected = NullPointerException.class)
    public void putExShouldThrowNullPointerExceptionWhenInternalMapIsNull() {
        // Arrange: Create a dictionary and force it into an invalid state.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName anyKey = PdfName.BLINDS;
        PdfName anyValue = new PdfName("AnyValue");

        // Simulate a corrupt state by nullifying the internal map.
        // This relies on the 'hashMap' field being package-private or protected.
        dictionary.hashMap = null;

        // Act: Attempt to use the method on the corrupted object.
        // This call is expected to throw a NullPointerException.
        dictionary.putEx(anyKey, anyValue);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}