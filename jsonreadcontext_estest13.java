package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the initial state of a {@link JsonReadContext}.
 *
 * Note: The original test class name and inheritance are preserved.
 * In a real-world scenario, these would likely be renamed for clarity (e.g., JsonReadContextTest).
 */
public class JsonReadContext_ESTestTest13 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Verifies that a newly created root JsonReadContext is initialized with the correct default state.
     * This includes having no current name, a nesting depth of 0, a "root" type description,
     * and an entry count of 0.
     */
    @Test
    public void newRootContextShouldHaveCorrectInitialState() {
        // Arrange: Create a new root context. The duplicate detector is not relevant for this test.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);

        // Act & Assert: Check the properties of the newly created context.
        assertFalse("A new root context should not have a current name yet", rootContext.hasCurrentName());
        assertEquals("Nesting depth of the root context should be 0", 0, rootContext.getNestingDepth());
        assertEquals("Type description of the root context should be 'root'", "root", rootContext.typeDesc());
        assertEquals("Initial entry count of the root context should be 0", 0, rootContext.getEntryCount());
    }
}