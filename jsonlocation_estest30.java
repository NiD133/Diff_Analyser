package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that appendOffsetDescription() correctly appends the formatted
     * line and column number to a StringBuilder that already contains content.
     */
    @Test
    public void appendOffsetDescription_shouldAppendFormattedLineAndColumn_toExistingContent() {
        // Arrange
        final int line = 314;
        final int column = 314;
        final String initialContent = "Location details: ";
        
        // The source reference can be null, and the character offset is not used by the
        // appendOffsetDescription method.
        JsonLocation location = new JsonLocation(null, -1L, line, column);
        
        StringBuilder reportBuilder = new StringBuilder(initialContent);
        String expectedReport = initialContent + "line: " + line + ", column: " + column;

        // Act
        location.appendOffsetDescription(reportBuilder);

        // Assert
        assertEquals(expectedReport, reportBuilder.toString());
    }
}