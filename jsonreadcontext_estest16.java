package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its state
 * and location tracking capabilities.
 */
public class JsonReadContextTest {

    /**
     * Tests that creating a root context and then retrieving its start location
     * returns a {@link JsonLocation} object with the correct line and column numbers,
     * and that the context itself is initialized with the expected root-level properties.
     */
    @Test
    public void getStartLocationShouldReturnCorrectLocationForRootContext() {
        // Arrange
        final int expectedLineNr = 1;
        final int expectedColNr = 10;
        // The DupDetector is required for context creation but its behavior is not under test here.
        // A null JsonParser is acceptable for creating a root-level detector.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);

        // Act
        JsonReadContext rootContext = JsonReadContext.createRootContext(expectedLineNr, expectedColNr, dupDetector);
        // The source object parameter for getStartLocation is used to create a ContentReference.
        // For this test, any non-null object will suffice.
        JsonLocation startLocation = rootContext.getStartLocation(new Object());

        // Assert
        // 1. Verify the properties of the created JsonReadContext
        assertTrue("A newly created root context should be in the root state.", rootContext.inRoot());
        assertEquals("The nesting depth of a root context should be 0.", 0, rootContext.getNestingDepth());
        assertEquals("The entry count of a new context should be 0.", 0, rootContext.getEntryCount());

        // 2. Verify the properties of the returned JsonLocation
        assertEquals("The line number should match the value provided at creation.", expectedLineNr, startLocation.getLineNr());
        assertEquals("The column number should match the value provided at creation.", expectedColNr, startLocation.getColumnNr());
        assertEquals("The character offset should be -1 when not applicable.", -1L, startLocation.getCharOffset());
    }
}