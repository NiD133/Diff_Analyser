package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that creating a root context correctly initializes its state
     * and that its start location reflects the provided coordinates.
     */
    @Test
    public void rootContextShouldHaveCorrectInitialStateAndLocation() {
        // Arrange
        final int expectedLine = 11;
        final int expectedColumn = 11;
        // The source object is used to create a reference in the JsonLocation.
        // A simple string is clearer than passing the context itself.
        final Object sourceRef = "test-source";

        // Act
        JsonReadContext rootContext = JsonReadContext.createRootContext(expectedLine, expectedColumn, null);
        JsonLocation startLocation = rootContext.getStartLocation(sourceRef);

        // Assert: Verify the state of the newly created root context
        assertTrue("A newly created root context should be 'inRoot'.", rootContext.inRoot());
        assertEquals("A root context should have a nesting depth of 0.", 0, rootContext.getNestingDepth());
        assertEquals("A new context should have an entry count of 0.", 0, rootContext.getEntryCount());

        // Assert: Verify the properties of the JsonLocation
        assertEquals("The line number should match the value provided at creation.",
                expectedLine, startLocation.getLineNr());
        assertEquals("The column number should match the value provided at creation.",
                expectedColumn, startLocation.getColumnNr());
        assertEquals("The character offset should be -1 for a new context.",
                -1L, startLocation.getCharOffset());
    }
}