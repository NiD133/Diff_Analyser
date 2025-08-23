package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonWriteContext} class.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that calling `withDupDetector()` on a context does not reset its
     * internal state, such as the entry count.
     */
    @Test
    public void withDupDetectorShouldPreserveExistingEntryCount() {
        // Arrange: Create a root context and write two values to it,
        // establishing an initial entry count of 2.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeValue();
        context.writeValue();

        // Verify the pre-condition
        assertEquals("Pre-condition failed: Entry count should be 2 before the call.", 2, context.getEntryCount());

        // Act: Call the method under test.
        context.withDupDetector(dupDetector);

        // Assert: The entry count should remain unchanged.
        assertEquals("The entry count should not be reset by withDupDetector.", 2, context.getEntryCount());
    }
}