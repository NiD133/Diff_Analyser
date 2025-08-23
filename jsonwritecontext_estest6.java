package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state management.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that the reset() method correctly re-initializes a root context,
     * maintaining its root state properties.
     */
    @Test
    public void whenResettingRootContext_thenStateIsMaintainedAsRoot() {
        // Arrange: Create a root context.
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Object testValue = new Object();

        // Act: Reset the context, re-initializing it as a root context with a new value.
        JsonWriteContext returnedContext = context.reset(JsonStreamContext.TYPE_ROOT, testValue);

        // Assert:
        // 1. The reset() method should return the same instance to allow for method chaining.
        assertSame("The reset method should return the same context instance.", context, returnedContext);

        // 2. The context should still be a valid root context.
        assertTrue("Context should remain in the root state after reset.", context.inRoot());
        assertEquals("Nesting depth for a root context should be 0.", 0, context.getNestingDepth());
        assertEquals("Entry count should be reset to 0.", 0, context.getEntryCount());
        
        // 3. The current value should be updated to the one provided.
        assertSame("The current value should be updated by reset().", testValue, context.getCurrentValue());
    }
}