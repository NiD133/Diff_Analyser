package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonLocation} class, focusing on its
 * description-generation methods.
 */
public class JsonLocationTest {

    /**
     * Tests that {@link JsonLocation#appendOffsetDescription(StringBuilder)}
     * correctly formats the output string when the location has a valid line and
     * column number.
     */
    @Test
    public void appendOffsetDescription_withValidLineAndColumn_appendsFormattedString() {
        // Arrange
        final int lineNumber = 15;
        final int columnNumber = 8;
        final String expectedDescription = "line: 15, column: 8";

        // The source reference and byte/char offsets are not relevant for this test case,
        // so we use "unknown" values (-1) for them.
        JsonLocation location = new JsonLocation(ContentReference.unknown(), -1L, -1L, lineNumber, columnNumber);
        StringBuilder stringBuilder = new StringBuilder();

        // Act
        location.appendOffsetDescription(stringBuilder);

        // Assert
        assertEquals(expectedDescription, stringBuilder.toString());
    }
}