package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that the reset() method correctly updates the state of a root context
     * without changing its fundamental properties, such as being the root.
     */
    @Test
    public void shouldResetStateWhileRemainingRootContext() {
        // Arrange: Create a root context. Its initial state is not important.
        JsonReadContext context = JsonReadContext.createRootContext(null);

        // Define the new state for the reset operation.
        final int newType = JsonStreamContext.TYPE_ROOT;
        final int newLine = 10;
        final int newColumn = 5;

        // Act: Reset the context with the new state.
        context.reset(newType, newLine, newColumn);

        // Assert: Verify that the context is still a root context and its state is updated.
        assertTrue("Context should remain a root context after reset", context.inRoot());
        assertEquals("Nesting depth should be 0 for a root context", 0, context.getNestingDepth());
        assertEquals("Entry count should be reset to 0", 0, context.getEntryCount());

        // Also, verify that the location information was correctly updated.
        JsonLocation location = context.startLocation(ContentReference.unknown());
        assertEquals("Line number should be updated to the new value", newLine, location.getLineNr());
        assertEquals("Column number should be updated to the new value", newColumn, location.getColumnNr());
    }
}