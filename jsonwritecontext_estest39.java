package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on its state transitions.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that writing a field name to a root context correctly transitions
     * its state to expect a value, and that its structural properties remain unchanged.
     */
    @Test
    public void writeFieldNameOnRootContext_shouldReturnStatusExpectValue() throws JsonProcessingException {
        // Arrange: Create a root-level JsonWriteContext.
        // A DupDetector is required, but its behavior is not under test here.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);

        // Assert initial state for clarity
        assertEquals("Initial state should be root", "root", rootContext.typeDesc());
        assertEquals("Initial nesting depth should be 0", 0, rootContext.getNestingDepth());
        assertEquals("Initial entry count should be 0", 0, rootContext.getEntryCount());

        // Act: Write a field name to the context. The name itself (null in this case)
        // is not critical for testing this specific state transition.
        int status = rootContext.writeFieldName(null);

        // Assert: The context should now expect a value, and its core properties should be unchanged.
        assertEquals("Status should indicate a value is expected next",
                JsonWriteContext.STATUS_EXPECT_VALUE, status);

        // The context is still the root context with no entries.
        assertEquals("Type description should remain 'root'", "root", rootContext.typeDesc());
        assertEquals("Nesting depth should still be 0", 0, rootContext.getNestingDepth());
        assertEquals("Entry count is not incremented until a value is written",
                0, rootContext.getEntryCount());
    }
}