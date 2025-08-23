package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that calling the merge() method with a null argument
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void merge_shouldThrowNullPointerException_whenArgumentIsNull() {
        // Arrange: Create an instance of the class under test.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Call the method with a null argument. The test framework will
        // assert that a NullPointerException is thrown.
        dictionary.merge(null);
    }
}