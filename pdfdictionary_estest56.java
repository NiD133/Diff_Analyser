package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the constructor that accepts an initial capacity
     * correctly sets the object's type to DICTIONARY.
     */
    @Test
    public void constructorWithCapacity_shouldSetObjectTypeToDictionary() {
        // Arrange: Create a PdfDictionary with a specific initial capacity.
        // The capacity value itself is not important for this test.
        int initialCapacity = 3;
        PdfDictionary dictionary = new PdfDictionary(initialCapacity);

        // Act: Get the type of the created object.
        int objectType = dictionary.type();

        // Assert: The type should match the constant for a dictionary object.
        assertEquals("A PdfDictionary object should have the type DICTIONARY.",
                     PdfObject.DICTIONARY, objectType);
    }
}