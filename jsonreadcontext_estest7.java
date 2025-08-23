package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on its state management.
 */
public class JsonReadContextTest {

    /**
     * Tests that calling expectComma() correctly increments the internal entry count.
     * The entry count tracks how many values have been processed in the current context.
     */
    @Test
    public void expectCommaShouldIncrementEntryCount() {
        // Arrange: Create a root-level read context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);

        // Assert initial state: A new context should have an entry count of 0.
        assertEquals("A newly created context should have an entry count of 0.", 0, context.getEntryCount());

        // Act: Simulate processing two elements. In JSON, this would correspond to
        // reading a value and expecting a comma before the next one.
        context.expectComma();
        context.expectComma();

        // Assert: The entry count should reflect that two elements have been processed.
        assertEquals("Entry count should be 2 after two calls to expectComma.", 2, context.getEntryCount());
    }
}