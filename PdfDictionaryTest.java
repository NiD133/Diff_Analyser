package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Focused tests for PdfDictionary behavior when a null key is passed.
 * 
 * These tests use clear Arrange-Act-Assert sections and explicit assertions/messages
 * to make intent and expected outcomes obvious.
 */
public class PdfDictionaryNullKeyBehaviorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final PdfName SAMPLE_VALUE = new PdfName("sample");

    @Test
    public void getReturnsNullWhenKeyIsNull() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();

        // Act
        PdfObject value = dictionary.get(null);

        // Assert
        assertNull("get(null) must return null", value);
    }

    @Test
    public void containsReturnsFalseWhenKeyIsNull() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();

        // Act
        boolean contains = dictionary.contains(null);

        // Assert
        assertFalse("contains(null) must be false", contains);
    }

    @Test
    public void removeWithNullKeyDoesNotChangeDictionaryState() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        PdfName existingKey = new PdfName("ExistingKey");
        dictionary.put(existingKey, SAMPLE_VALUE); // add a valid mapping
        int sizeBefore = dictionary.size();

        // Act
        dictionary.remove(null);

        // Assert
        assertEquals("remove(null) must not change size", sizeBefore, dictionary.size());
        assertTrue("Existing mapping must not be affected by remove(null)", dictionary.contains(existingKey));
    }

    @Test
    public void putThrowsIAEWhenKeyIsNull() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("key is null.");

        // Act
        dictionary.put(null, SAMPLE_VALUE);
    }

    @Test
    public void putExThrowsIAEWhenKeyIsNull() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("key is null.");

        // Act
        dictionary.putEx(null, SAMPLE_VALUE);
    }
}