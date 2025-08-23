package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its initial state and location reporting.
 */
public class JsonReadContextTest {

    /**
     * Tests that a newly created root context has the expected default state
     * and that its start location is initialized correctly.
     */
    @Test
    public void getStartLocationForRootContextShouldReturnDefaultLocation() {
        // Arrange: Create a root-level context.
        // A DupDetector is needed, but for this test, it can be a simple root detector.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);

        // Act: Retrieve the starting location from the context.
        // The source object (here, the context itself) is used to construct the location's ContentReference.
        JsonLocation startLocation = rootContext.getStartLocation(rootContext);

        // Assert: Verify the properties of both the context and its location.

        // 1. Check the context's state
        assertEquals("Context type should be 'ROOT'", "ROOT", rootContext.getTypeDesc());
        assertEquals("Root context nesting depth should be 0", 0, rootContext.getNestingDepth());
        assertEquals("Root context initial entry count should be 0", 0, rootContext.getEntryCount());

        // 2. Check the location's coordinates
        assertEquals("Initial line number should be 1", 1, startLocation.getLineNr());
        assertEquals("Initial column number should be 0", 0, startLocation.getColumnNr());
        assertEquals("Initial character offset should be -1", -1L, startLocation.getCharOffset());
    }
}