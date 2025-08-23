package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonLocation} class, focusing on its constructor and basic property accessors.
 */
public class JsonLocationTest {

    /**
     * Verifies that the JsonLocation constructor correctly initializes all properties
     * (byte offset, char offset, line number, and column number), and that the
     * corresponding getter methods return these exact values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the input parameters for the JsonLocation object.
        final ContentReference contentReference = ContentReference.redacted();
        final long expectedByteOffset = 1852L;
        final long expectedCharOffset = -413L;
        final int expectedLineNumber = 0;
        final int expectedColumnNumber = 74;

        // Act: Create a new JsonLocation instance with the specified parameters.
        JsonLocation jsonLocation = new JsonLocation(contentReference,
                expectedByteOffset,
                expectedCharOffset,
                expectedLineNumber,
                expectedColumnNumber);

        // Assert: Verify that each getter returns the value passed to the constructor.
        assertEquals("The byte offset should match the value provided in the constructor.",
                expectedByteOffset, jsonLocation.getByteOffset());
        assertEquals("The char offset should match the value provided in the constructor.",
                expectedCharOffset, jsonLocation.getCharOffset());
        assertEquals("The line number should match the value provided in the constructor.",
                expectedLineNumber, jsonLocation.getLineNr());
        assertEquals("The column number should match the value provided in the constructor.",
                expectedColumnNumber, jsonLocation.getColumnNr());
    }
}