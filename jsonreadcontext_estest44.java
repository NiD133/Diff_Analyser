package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its initial state
 * upon construction.
 */
public class JsonReadContextTest {

    /**
     * Tests that a root-level object context is correctly initialized with expected default values
     * when using its constructor. This verifies the initial nesting depth, context type, and entry count.
     */
    @Test
    public void constructor_whenCreatingRootObjectContext_initializesStateCorrectly() {
        // Arrange: Define clear and descriptive variables for constructor arguments.
        final int expectedLineNumber = 1;
        final int expectedColumnNumber = 0;

        // Act: Create a new root-level object context.
        // This test targets the constructor:
        // JsonReadContext(JsonReadContext parent, DupDetector dups, int type, int lineNr, int colNr)
        // We pass 'null' for the parent to signify a root context.
        JsonReadContext context = new JsonReadContext(null, null, JsonStreamContext.TYPE_OBJECT,
                expectedLineNumber, expectedColumnNumber);

        // Assert: Verify the state of the newly created context.
        assertEquals("Nesting depth for a root context should be 0", 0, context.getNestingDepth());
        assertTrue("Context should be identified as an object", context.inObject());
        assertEquals("A new context should have 0 entries", 0, context.getEntryCount());
    }
}