package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its initial state
 * and location reporting capabilities.
 */
public class JsonReadContextTest {

    @Test
    public void getStartLocationForRootContextShouldReturnCorrectLocationDetails() {
        // Arrange: Create a root context at a specific line and column.
        final int expectedLine = 0;
        final int expectedColumn = 1;
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(expectedLine, expectedColumn, dupDetector);

        // Act: Get the start location.
        // The getStartLocation(Object) method is deprecated, but we test its behavior.
        // It creates a JsonLocation from the context's line/col and the provided source (null here).
        JsonLocation startLocation = rootContext.getStartLocation(null);

        // Assert: Verify the initial state of the root context and the details of the start location.

        // 1. Check the properties of the root context itself.
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());

        // 2. Check the properties of the returned JsonLocation.
        assertEquals(expectedLine, startLocation.getLineNr());
        assertEquals(expectedColumn, startLocation.getColumnNr());
        // Character offset is -1 because no source reference was provided to getStartLocation().
        assertEquals(-1L, startLocation.getCharOffset());
    }
}