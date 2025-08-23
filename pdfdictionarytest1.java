package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    @Test
    public void get_whenKeyIsNull_shouldReturnNull() {
        // Arrange: Create an empty dictionary.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Attempt to retrieve a value using a null key.
        // The get() method is specified to take a PdfName, but null is a valid
        // input for any reference type, representing an important edge case.
        PdfObject retrievedValue = dictionary.get(null);

        // Assert: The returned value should be null.
        // The underlying implementation relies on a HashMap, which returns null
        // for a key that is not present, including a null key.
        assertThat(retrievedValue).isNull();
    }
}