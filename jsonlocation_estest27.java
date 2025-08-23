package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class focuses on the behavior of the JsonLocation class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class JsonLocation_ESTestTest27 { // In a real-world scenario, this class would be renamed to JsonLocationTest

    /**
     * Tests that sourceDescription() throws an exception when the location's character
     * offset is far beyond the end of the actual source content.
     */
    @Test
    public void sourceDescription_shouldThrowException_whenOffsetIsBeyondEmptyContent() {
        // Arrange: Set up a JsonLocation with an empty content source but a large character offset.
        // The sourceDescription() method tries to create a snippet of the source around the
        // location. When the offset is far beyond the actual content length, an indexing
        // error is expected.

        StringBuilder emptyContent = new StringBuilder("");
        long charOffsetFarBeyondContent = 500L;
        long byteOffset = 8L;
        int lineNumber = 1571;
        int columnNumber = 500;

        // The ErrorReportConfiguration is needed to construct the ContentReference.
        // Its values are not critical to this specific test's outcome.
        ErrorReportConfiguration reportConfig = new ErrorReportConfiguration(8, -1);
        ContentReference contentRef = ContentReference.construct(true, emptyContent, reportConfig);

        // Create the location pointing to an offset (500) that is out of bounds for the empty content (length 0).
        JsonLocation location = new JsonLocation(contentRef, byteOffset, charOffsetFarBeyondContent, lineNumber, columnNumber);

        // Act & Assert
        try {
            location.sourceDescription();
            fail("Expected a StringIndexOutOfBoundsException because the character offset is outside the bounds of the source content.");
        } catch (StringIndexOutOfBoundsException e) {
            // The underlying implementation attempts to access the content at a calculated
            // negative index, which is the expected behavior in this scenario.
            assertEquals("String index out of range: -1", e.getMessage());
        }
    }
}