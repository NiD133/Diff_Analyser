package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on its state management
 * within different JSON structures.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that writing multiple values into an array context correctly increments
     * the internal index and returns the expected status. For any element after the
     * first, the status should indicate that a comma separator is required.
     */
    @Test
    public void writeValue_inArrayContext_shouldIncrementIndexAndReturnStatusOkAfterComma() {
        // Arrange: Create a root context and a child array context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(null);
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();

        // Act: Write three values. The status of the first two are ignored as we
        // are interested in the state after the first element.
        arrayContext.writeValue(); // Writes the first value, index becomes 0.
        arrayContext.writeValue(); // Writes the second value, index becomes 1.
        int statusAfterThirdValue = arrayContext.writeValue(); // Writes the third value, index becomes 2.

        // Assert: Check the index and the status returned by the third write.
        // The index is 0-based, so after writing three values, it should be 2.
        assertEquals("After writing three values, the current index should be 2",
                2, arrayContext.getCurrentIndex());

        // For any element after the first in an array, the status should be STATUS_OK_AFTER_COMMA.
        assertEquals("Status for a subsequent array element should indicate a comma is needed",
                JsonWriteContext.STATUS_OK_AFTER_COMMA, statusAfterThirdValue);
    }
}