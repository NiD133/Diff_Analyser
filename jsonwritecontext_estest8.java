package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains unit tests for the {@link JsonWriteContext} class, focusing on its state management.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that calling the {@code reset} method on a root context correctly
     * changes its type and updates its state to no longer be a root context.
     */
    @Test
    public void resetOnRootContextShouldChangeTypeAndClearRootStatus() {
        // Arrange: Create a root context and verify its initial state.
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertTrue("A newly created context should initially be in the root state.", context.inRoot());

        // Act: Reset the context to represent an array.
        JsonWriteContext resetContext = context.reset(JsonStreamContext.TYPE_ARRAY, null);

        // Assert: Verify the context's state has been updated correctly.
        assertSame("The reset() method should return the same instance for chaining.", context, resetContext);
        assertFalse("After resetting, the context should no longer be in the root state.", resetContext.inRoot());
        assertTrue("The context type should be updated to an array.", resetContext.inArray());
    }
}