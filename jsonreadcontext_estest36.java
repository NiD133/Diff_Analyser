package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that the reset() method correctly updates the context's type and location,
     * effectively reusing the context object for a new JSON structure.
     */
    @Test
    public void resetShouldUpdateContextTypeAndLocation() {
        // Arrange: Create an initial root context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);

        // Assert initial state: The context should be a root context.
        assertTrue("Context should initially be in the root state", context.inRoot());
        assertEquals("Initial type description should be ROOT", "ROOT", context.getTypeDesc());

        // Act: Reset the context to represent an array starting at a new location.
        final int newType = JsonStreamContext.TYPE_ARRAY;
        final int newLine = 5;
        final int newCol = 10;
        context.reset(newType, newLine, newCol);

        // Assert: The context's state should be updated to reflect the new type and location.
        assertEquals("After reset, type description should be ARRAY", "ARRAY", context.getTypeDesc());
        assertFalse("Context should no longer be in the root state after being reset to an array", context.inRoot());

        // Verify that the start location was also updated correctly.
        JsonLocation startLocation = context.startLocation(ContentReference.unknown());
        assertEquals("Line number should be updated after reset", newLine, startLocation.getLineNr());
        assertEquals("Column number should be updated after reset", newCol, startLocation.getColumnNr());
    }
}