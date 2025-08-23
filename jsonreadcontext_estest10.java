package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that creating a root context and then getting its start location
     * results in a correctly configured context and a default location.
     */
    @Test
    public void startLocationForRootContextShouldReturnDefaultLocation() {
        // Arrange: Create a root-level context.
        // The createRootContext() factory method is the entry point for parsing.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        ContentReference contentReference = ContentReference.redacted();

        // Act: Get the starting location from the context.
        JsonLocation location = rootContext.startLocation(contentReference);

        // Assert: Verify the state of both the context and the location object.
        assertNotNull("The returned location should not be null.", location);

        // 1. Verify the initial state of the root context.
        assertEquals("Type description should be 'ROOT'", "ROOT", rootContext.getTypeDesc());
        assertEquals("Nesting depth should be 0 for the root context", 0, rootContext.getNestingDepth());
        assertEquals("Entry count should be 0 initially", 0, rootContext.getEntryCount());

        // 2. Verify the properties of the generated JsonLocation.
        // A default root context starts at line 1, column 0, with an unknown character offset.
        assertEquals("Default line number should be 1", 1, location.getLineNr());
        assertEquals("Default column number should be 0", 0, location.getColumnNr());
        assertEquals("Character offset should be -1 (unknown)", -1L, location.getCharOffset());
    }
}