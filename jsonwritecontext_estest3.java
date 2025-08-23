package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state transitions
 * and behavior when writing JSON components.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that calling {@code writeFieldName} on a context that is not a JSON object
     * (e.g., an array) correctly returns the {@code STATUS_EXPECT_VALUE} status code.
     * This behavior is expected because field names are only valid within an object structure.
     */
    @Test
    public void writeFieldName_whenContextIsNotObject_shouldReturnExpectValueStatus() throws Exception {
        // Arrange: Create a context and put it into a non-object state.
        // An array context is a good example of a state where writing a field name is invalid.
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.reset(JsonStreamContext.TYPE_ARRAY);

        // Act: Attempt to write a field name, which should be an invalid operation
        // for an array context.
        int status = context.writeFieldName("anyFieldName");

        // Assert: The returned status should indicate that a value was expected,
        // as field names are not allowed in an array context.
        assertEquals("Status should indicate a value is expected",
                JsonWriteContext.STATUS_EXPECT_VALUE, status);

        // A secondary check to ensure the context's state was correctly updated after the reset.
        assertFalse("Context should no longer be in the root state", context.inRoot());
    }
}