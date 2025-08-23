package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that setting a current value on a newly created root context
     * does not alter its fundamental properties like entry count, root status,
     * or nesting depth.
     */
    @Test
    public void setCurrentValueOnRootContextShouldNotChangeItsState() {
        // Arrange: Create a new root context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object testValue = new Object();

        // Act: Set a current value on the context.
        rootContext.setCurrentValue(testValue);

        // Assert: Verify that the context's state remains consistent with a root context.
        assertEquals("Entry count should remain 0", 0, rootContext.getEntryCount());
        assertTrue("Context should still be in the root", rootContext.inRoot());
        assertEquals("Nesting depth should remain 0", 0, rootContext.getNestingDepth());

        // Also, confirm the value was set correctly.
        assertSame("getCurrentValue() should return the object that was set",
                testValue, rootContext.getCurrentValue());
    }
}