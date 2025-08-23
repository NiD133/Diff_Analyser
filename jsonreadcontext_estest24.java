package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.json.JsonReadContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Verifies that a newly created root context is initialized with the correct
     * default properties, such as type, nesting depth, and entry count.
     */
    @Test
    public void rootContextShouldHaveCorrectInitialState() {
        // Arrange: Create a root context. A null DupDetector is used as we are not
        // testing duplicate detection in this specific scenario.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);

        // Assert: Verify the initial state of the root context.
        assertEquals("The type description for a root context should be 'ROOT'.",
                "ROOT", rootContext.getTypeDesc());
        assertEquals("The nesting depth for a root context should be 0.",
                0, rootContext.getNestingDepth());
        assertEquals("A new root context should have an entry count of 0.",
                0, rootContext.getEntryCount());
        assertNull("A new root context should not have a current name.",
                rootContext.getCurrentName());
    }
}