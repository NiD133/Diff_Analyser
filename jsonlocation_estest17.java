package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the JsonLocation class.
 */
public class JsonLocationTest {

    /**
     * Verifies that the constructor correctly initializes a JsonLocation object
     * and that the line and column numbers can be retrieved via their respective getters.
     */
    @Test
    public void constructorShouldCorrectlySetLineAndColumnNumbers() {
        // Arrange: Define the input parameters for the JsonLocation.
        Object sourceReference = new Object();
        long totalBytes = 0L;
        long totalChars = 0L;
        int expectedLineNumber = -1678;
        int expectedColumnNumber = -1678;

        // Act: Create a new JsonLocation instance.
        JsonLocation location = new JsonLocation(
            sourceReference,
            totalBytes,
            totalChars,
            expectedLineNumber,
            expectedColumnNumber
        );

        // Assert: Check that the getters return the values provided to the constructor.
        assertEquals("The line number should match the value provided in the constructor.",
                expectedLineNumber, location.getLineNr());
        assertEquals("The column number should match the value provided in the constructor.",
                expectedColumnNumber, location.getColumnNr());
    }
}