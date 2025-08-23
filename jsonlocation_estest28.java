package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that appendOffsetDescription() correctly appends the byte offset
     * to a StringBuilder when line and column information is not available.
     * The byte offset should be preferred over the character offset in this case.
     */
    @Test
    public void appendOffsetDescriptionShouldAppendByteOffsetWhenLineNumberIsUnavailable() {
        // Arrange: Create a location with a valid byte offset and char offset,
        // but an invalid line number (-1).
        final long byteOffset = 123L;
        final long charOffset = 456L; // This should be ignored in favor of the byte offset.
        final int unavailableLineNumber = -1;
        final int unavailableColumnNumber = -1;

        JsonLocation location = new JsonLocation(
            /* sourceRef */ null,
            byteOffset,
            charOffset,
            unavailableLineNumber,
            unavailableColumnNumber
        );

        String initialContent = "Source: [UNKNOWN]; ";
        StringBuilder stringBuilder = new StringBuilder(initialContent);

        // Act: Call the method under test.
        location.appendOffsetDescription(stringBuilder);

        // Assert: Verify that the byte offset description was appended correctly.
        String expectedDescription = "byte offset: #" + byteOffset;
        String expectedFullString = initialContent + expectedDescription;
        assertEquals(expectedFullString, stringBuilder.toString());
    }
}