package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its constructors and accessors.
 */
public class JsonLocationTest {

    /**
     * Tests that the four-argument constructor correctly initializes the location's properties,
     * including setting the default byte offset.
     *
     * This test covers a deprecated constructor to ensure backward compatibility is maintained.
     */
    @Test
    public void constructorWithObjectSourceShouldCorrectlySetLocationFields() {
        // Arrange
        final Object sourceRef = null; // The source reference can be null.
        final long expectedCharOffset = -219L;
        final int expectedLineNumber = 314;
        final int expectedColumnNumber = 314;
        // This specific deprecated constructor defaults the byte offset to -1.
        final long expectedByteOffset = -1L;

        // Act
        // We are testing the deprecated constructor:
        // JsonLocation(Object srcRef, long totalChars, int lineNr, int columnNr)
        @SuppressWarnings("deprecation")
        JsonLocation location = new JsonLocation(sourceRef, expectedCharOffset, expectedLineNumber, expectedColumnNumber);

        // Assert
        assertEquals("Line number should match the constructor argument.", expectedLineNumber, location.getLineNr());
        assertEquals("Column number should match the constructor argument.", expectedColumnNumber, location.getColumnNr());
        assertEquals("Character offset should match the constructor argument.", expectedCharOffset, location.getCharOffset());
        assertEquals("Byte offset should default to -1 for this constructor.", expectedByteOffset, location.getByteOffset());
    }
}