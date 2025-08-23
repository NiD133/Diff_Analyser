package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state
 * management during JSON writing.
 */
public class JsonWriteContextTest {

    /**
     * Tests that calling {@code writeValue()} on a new root context correctly
     * updates the state. It should increment the value count and return the
     * status indicating that the write was successful without needing a separator.
     */
    @Test
    public void writeValue_onNewRootContext_returnsStatusOKAndIncrementsCount() {
        // Arrange: Create a new root-level JSON write context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act: Simulate writing the first value in the context.
        int writeStatus = rootContext.writeValue();

        // Assert: Verify the context's state is updated as expected.

        // The first call to writeValue in a root context should return STATUS_OK_AS_IS.
        assertEquals("The write status should indicate success without needing a separator.",
                JsonWriteContext.STATUS_OK_AS_IS, writeStatus);

        // The entry count should now be 1.
        assertEquals("The entry count should be 1 after writing one value.",
                1, rootContext.getEntryCount());
    }
}