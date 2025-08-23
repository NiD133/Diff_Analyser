package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its initial state.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that a newly created root context is initialized with the correct default values.
     * A root context should have a null name, a nesting depth of 0, an entry count of 0,
     * and a type description of "ROOT".
     */
    @Test
    public void rootContextShouldBeInitializedCorrectly() {
        // Arrange: Create a new root-level write context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Assert: Verify the initial state of the root context.
        assertNull("A new root context should not have a current name.", rootContext.getCurrentName());
        assertEquals("A new root context should have an entry count of 0.", 0, rootContext.getEntryCount());
        assertEquals("A new root context should have a nesting depth of 0.", 0, rootContext.getNestingDepth());
        assertEquals("The type description for a root context should be 'ROOT'.", "ROOT", rootContext.getTypeDesc());
    }
}