package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the instance reuse mechanism of the {@link JsonWriteContext}.
 */
public class JsonWriteContextTest {

    /**
     * Tests that `createChildObjectContext` correctly reuses and resets an existing
     * context instance available in the `_child` field. This is an internal
     * performance optimization.
     */
    @Test
    public void createChildObjectContext_whenChildInstanceExists_shouldReuseAndResetIt() {
        // Arrange: Create a root context and a child array context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext parentArrayContext = rootContext.createChildArrayContext(new Object());

        // To test the instance reuse mechanism, we manually set the parent's `_child`
        // field to an existing instance (the rootContext). This simulates a context
        // being available for reuse.
        parentArrayContext._child = rootContext;

        // Pre-condition check: The instance to be reused is not yet an "Object" context.
        assertNotEquals("Object", rootContext.typeDesc());

        // Act: Create a new object context. This should trigger the reuse of the
        // `rootContext` instance we placed in the `_child` field.
        parentArrayContext.createChildObjectContext(new Object());

        // Assert: Verify that the original rootContext instance was modified (reset)
        // to become the new object context, confirming it was reused.
        assertEquals("The reused context's type should be updated to 'Object'",
                     "Object", rootContext.typeDesc());
        
        // The parent context's state should remain unaffected.
        assertFalse("Parent context's index should not have been set",
                    parentArrayContext.hasCurrentIndex());
    }
}