package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class.
 */
public class JsonWriteContextTest {

    /**
     * Tests that calling clearAndGetParent() on a root context correctly returns null,
     * as a root context has no parent.
     */
    @Test
    public void clearAndGetParent_onRootContext_shouldReturnNull() {
        // Arrange: Create a root-level write context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act: Attempt to get the parent of the root context.
        JsonWriteContext parentContext = rootContext.clearAndGetParent();

        // Assert: The parent should be null, and the root context's state should be unchanged.
        assertNull("The parent of a root context must be null.", parentContext);
        assertTrue("The context should still be considered the root.", rootContext.inRoot());
        assertEquals("The nesting depth of the root context should remain 0.", 0, rootContext.getNestingDepth());
        assertEquals("The entry count of the root context should remain 0.", 0, rootContext.getEntryCount());
    }
}