package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonReadContext_ESTestTest40 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that calling expectComma() correctly increments the context's
     * internal index and entry count. This simulates parsing multiple
     * elements within a single JSON scope (like a root-level sequence).
     */
    @Test(timeout = 4000)
    public void expectCommaShouldIncrementIndexAndEntryCount() {
        // Arrange: Create a root-level JsonReadContext.
        // A DupDetector is a required dependency for the context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);

        // Assert initial state for clarity
        assertEquals("Initial entry count should be 0", 0, rootContext.getEntryCount());
        assertEquals("Initial index should be -1", -1, rootContext.getCurrentIndex());

        // Act: Simulate processing two values at the root level.
        // expectComma() is called before each subsequent value after the first.
        rootContext.expectComma(); // After processing the first value
        rootContext.expectComma(); // After processing the second value

        // Assert: Verify that the index and entry count have been updated correctly.
        // The entry count is the total number of values processed.
        assertEquals("Entry count should be 2 after two calls", 2, rootContext.getEntryCount());
        // The index points to the last value processed (0-based).
        assertEquals("Current index should be 1 after two calls", 1, rootContext.getCurrentIndex());
    }
}