package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that calling put() with a null key results in an IllegalArgumentException.
     * This is a requirement of the PDF specification, where dictionary keys must be non-null PdfName objects.
     */
    @Test
    public void put_withNullKey_throwsIllegalArgumentException() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        
        // Configure expectations for the exception
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("key is null.");

        // Act: This call is expected to throw the configured exception.
        dictionary.put(null, new PdfName("anyValue"));
        
        // Assert: The test will pass only if the expected exception is thrown.
        // The 'thrown' rule handles the assertion.
    }
}