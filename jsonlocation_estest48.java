package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonLocation} class, focusing on its
 * constructor and accessor methods.
 */
public class JsonLocationTest {

    /**
     * Verifies that a JsonLocation object, when created, correctly stores the
     * provided location details (line, column, offsets) and that its getter
     * methods return these values.
     */
    @Test
    public void testConstructorAndGetters() {
        // Arrange: Define the location parameters and create a JsonLocation instance.
        ContentReference dummyContentReference = ContentReference.redacted();
        final long expectedCharOffset = -578L;
        final int expectedLineNumber = 500;
        final int expectedColumnNumber = 500;
        
        // This test uses a deprecated constructor:
        // new JsonLocation(Object srcRef, long totalChars, int lineNr, int columnNr)
        // Internally, this constructor sets the byte offset to -1.
        JsonLocation location = new JsonLocation(dummyContentReference, expectedCharOffset, expectedLineNumber, expectedColumnNumber);

        // Act: Retrieve the properties from the created object using its getters.
        int actualColumnNumber = location.getColumnNr();
        int actualLineNumber = location.getLineNr();
        long actualCharOffset = location.getCharOffset();
        long actualByteOffset = location.getByteOffset();

        // Assert: Verify that the retrieved values match the expected values.
        assertEquals("Column number should match the constructor argument", expectedColumnNumber, actualColumnNumber);
        assertEquals("Line number should match the constructor argument", expectedLineNumber, actualLineNumber);
        assertEquals("Character offset should match the constructor argument", expectedCharOffset, actualCharOffset);
        assertEquals("Byte offset should be -1 for this specific constructor", -1L, actualByteOffset);
    }
}