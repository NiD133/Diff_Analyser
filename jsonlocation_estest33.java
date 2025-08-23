package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonLocation} class, focusing on its string representation methods.
 */
public class JsonLocationTest {

    /**
     * Verifies that appendOffsetDescription() formats the output with "line: UNKNOWN"
     * when the location is constructed with invalid (negative) line number and byte/char offsets.
     * This tests the fallback formatting logic.
     */
    @Test
    public void appendOffsetDescriptionShouldUseUnknownLineFormatForInvalidLocation() {
        // Arrange
        final int INVALID_LINE_NUMBER = -1;
        final int ARBITRARY_COLUMN_NUMBER = 0;
        final long INVALID_OFFSET = -1L;

        // The source object itself is not relevant for this test, only its presence.
        Object sourceObject = new Object();
        ContentReference contentReference = ContentReference.rawReference(sourceObject);

        // Create a JsonLocation with an invalid line number and invalid offsets to trigger
        // the fallback description logic.
        JsonLocation location = new JsonLocation(contentReference,
                INVALID_OFFSET,          // totalBytes
                INVALID_OFFSET,          // totalChars
                INVALID_LINE_NUMBER,     // lineNr
                ARBITRARY_COLUMN_NUMBER  // columnNr
        );

        StringBuilder resultBuilder = new StringBuilder();
        String expectedDescription = "line: UNKNOWN, column: " + ARBITRARY_COLUMN_NUMBER;

        // Act
        location.appendOffsetDescription(resultBuilder);

        // Assert
        assertEquals(expectedDescription, resultBuilder.toString());
    }
}