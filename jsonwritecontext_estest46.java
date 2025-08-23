package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

// Note: The original test class name and scaffolding are retained as per the prompt's context.
// In a real-world scenario, these would likely be renamed for clarity.
public class JsonWriteContext_ESTestTest46 extends JsonWriteContext_ESTest_scaffolding {

    /**
     * Tests the initial state of a root JsonWriteContext created with an unknown type.
     *
     * This test verifies that when a root-level context (i.e., one with no parent)
     * is instantiated with an arbitrary type code, its properties are set to the
     * expected default values.
     */
    @Test
    public void testInitialStateOfRootContextWithUnknownType() {
        // Arrange: Create a root context with an arbitrary, unknown type code.
        // A root context has no parent and, in this case, no duplicate detector.
        final int UNKNOWN_TYPE = 2033;
        JsonWriteContext context = new JsonWriteContext(UNKNOWN_TYPE, null, null);

        // Assert: Verify the initial state of the newly created context.
        assertNull("A newly created context should not have a current value.", context.getCurrentValue());
        assertEquals("The nesting depth of a root context should be 0.", 0, context.getNestingDepth());
        assertEquals("The initial entry count should be 0.", 0, context.getEntryCount());
        assertEquals("The type description for an unknown type should be '?'.", "?", context.typeDesc());
    }
}