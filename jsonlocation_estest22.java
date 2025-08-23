package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that the constructor correctly initializes all location properties
     * and that the getter methods return the expected values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the properties for our test location.
        final long expectedCharOffset = 3039L;
        final int expectedLineNumber = 575;
        final int expectedColumnNumber = -559; // The constructor accepts any integer value.
        final String sourceDescription = "test-source";
        final ContentReference contentReference = ContentReference.construct(false, sourceDescription);

        // Act: Create a new JsonLocation instance.
        // We use the constructor that does not take a byte offset, which should
        // result in a default byte offset of -1.
        JsonLocation location = new JsonLocation(contentReference, expectedCharOffset, expectedLineNumber, expectedColumnNumber);

        // Assert: Verify that all properties were set as expected.
        assertEquals("Line number should match the constructor argument",
                expectedLineNumber, location.getLineNr());
        assertEquals("Column number should match the constructor argument",
                expectedColumnNumber, location.getColumnNr());
        assertEquals("Character offset should match the constructor argument",
                expectedCharOffset, location.getCharOffset());
        assertEquals("Byte offset should default to -1 for this constructor",
                -1L, location.getByteOffset());

        // Also, verify that the content reference is the same instance that was passed in.
        assertSame("ContentReference should be the same object instance provided to the constructor",
                contentReference, location.contentReference());
    }
}