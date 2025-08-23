package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state management,
 * particularly the context reuse mechanism via the `reset` method.
 */
public class JsonWriteContextTest {

    /**
     * Tests that the `reset` method correctly re-initializes a child context.
     * <p>
     * This test verifies that when a child context is reset:
     * <ul>
     *     <li>Its type is updated (even to an unknown type).</li>
     *     <li>Its internal state, like the entry count, is cleared.</li>
     *     <li>It remains linked to its parent, preserving its nesting depth.</li>
     *     <li>The parent context remains unaffected.</li>
     * </ul>
     * This behavior is crucial for the instance-reuse optimization in Jackson.
     */
    @Test
    public void whenChildContextIsReset_thenTypeIsUpdatedAndStateIsCleared() {
        // Arrange: Create a root context and a child object context.
        // An arbitrary type not defined in JsonStreamContext to test the 'unknown' case.
        final int UNKNOWN_CONTEXT_TYPE = 5; 
        Object currentValue = new Object();
        
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext(currentValue);

        // Verify the initial state of the newly created child context.
        assertEquals("OBJECT", childObjectContext.getTypeDesc());
        assertEquals(0, childObjectContext.getEntryCount());
        assertEquals(1, childObjectContext.getNestingDepth());

        // Act: Reset the child context.
        // The `_child` field holds the reusable context instance, which is the same
        // instance as `childObjectContext`. We reset it to a new, unknown type.
        JsonWriteContext resetContext = rootContext._child.reset(UNKNOWN_CONTEXT_TYPE, currentValue);

        // Assert: Verify the state of the context after the reset.
        
        // 1. The reset operation should return the same instance for call-chaining.
        assertSame("Reset should return the same context instance.", childObjectContext, resetContext);

        // 2. The state of the reset context should be updated.
        assertEquals("Type description should be '?' for an unknown type.", "?", resetContext.getTypeDesc());
        assertEquals("Entry count should be reset to 0.", 0, resetContext.getEntryCount());
        assertEquals("Nesting depth should be preserved as the parent is unchanged.", 1, resetContext.getNestingDepth());

        // 3. The state of the parent (root) context should be unaffected.
        assertTrue("Root context should still be a root context.", rootContext.inRoot());
        assertEquals("Root context entry count should remain 0.", 0, rootContext.getEntryCount());
    }
}