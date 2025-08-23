package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that the {@link JsonLocation#toString()} method produces a concise and
     * informative string when the source reference is null.
     * <p>
     * The expected format should indicate an "UNKNOWN" source and include the byte offset,
     * while omitting other details like line or column number, even if they are provided.
     */
    @Test
    public void toString_withNullSource_shouldDescribeSourceAsUnknownAndShowByteOffset() {
        // Arrange
        Object sourceRef = null;
        long byteOffset = 62L;
        long charOffset = 500L;
        int lineNumber = -2898;
        int columnNumber = -26;

        String expectedDescription = "[Source: UNKNOWN; byte offset: #62]";

        // Act
        // Note: Using the deprecated constructor to match the original test's scenario.
        JsonLocation location = new JsonLocation(sourceRef, byteOffset, charOffset, lineNumber, columnNumber);
        String actualDescription = location.toString();

        // Assert
        // 1. Verify the primary behavior: the string representation.
        assertEquals(expectedDescription, actualDescription);

        // 2. Verify the constructor correctly assigned all properties.
        assertEquals(charOffset, location.getCharOffset());
        assertEquals(lineNumber, location.getLineNr());
        assertEquals(columnNumber, location.getColumnNr());
    }
}