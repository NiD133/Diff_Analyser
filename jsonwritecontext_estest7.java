package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state management.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that the reset() method correctly changes the context's type.
     * The test creates a root context, resets it to an object type, and asserts
     * that the type description is updated as expected.
     */
    @Test
    public void resetShouldChangeContextTypeToObject() {
        // Arrange: Create a root context, which is the default initial state.
        JsonWriteContext context = JsonWriteContext.createRootContext(null);
        assertEquals("Initial context type should be ROOT", "ROOT", context.getTypeDesc());

        // Act: Reset the context to an OBJECT type.
        // We use the constant for clarity instead of the magic number '2'.
        context.reset(JsonStreamContext.TYPE_OBJECT, null);

        // Assert: Verify that the context type has been updated to OBJECT.
        assertEquals("Context type should be OBJECT after reset", "OBJECT", context.getTypeDesc());
        assertTrue("Context should be considered 'inObject' after reset", context.inObject());
    }
}