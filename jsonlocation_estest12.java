package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its data-holding properties.
 */
public class JsonLocationTest {

    /**
     * Tests that the constructor correctly initializes all location properties
     * and that the corresponding getter methods return those values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the expected location properties.
        final long expectedByteOffset = -4198L;
        final long expectedCharOffset = -4198L;
        final int expectedLineNumber = -1143;
        final int expectedColumnNumber = 0;
        final Object sourceObject = new Object();
        final ContentReference contentReference = ContentReference.rawReference(true, sourceObject);

        // Act: Create a new JsonLocation instance with the defined properties.
        JsonLocation location = new JsonLocation(contentReference, expectedByteOffset, expectedCharOffset, expectedLineNumber, expectedColumnNumber);

        // Assert: Verify that each getter returns the value passed to the constructor.
        assertEquals("Byte offset should match the constructor argument",
                expectedByteOffset, location.getByteOffset());

        assertEquals("Character offset should match the constructor argument",
                expectedCharOffset, location.getCharOffset());

        assertEquals("Line number should match the constructor argument",
                expectedLineNumber, location.getLineNr());

        assertEquals("Column number should match the constructor argument",
                expectedColumnNumber, location.getColumnNr());
    }
}