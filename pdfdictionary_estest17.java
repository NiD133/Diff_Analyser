package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the mergeDifferent() method throws a NullPointerException
     * when a null dictionary is passed as an argument. This is the expected
     * behavior to prevent merging with a non-existent object.
     */
    @Test(expected = NullPointerException.class)
    public void mergeDifferent_withNullArgument_shouldThrowNullPointerException() {
        // Arrange: Create an instance of the class under test.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Call the method with a null argument, which is expected to throw an exception.
        dictionary.mergeDifferent(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}