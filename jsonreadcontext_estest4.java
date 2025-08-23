package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that `withDupDetector()` creates a new context instance
     * while preserving the state of the original context.
     */
    @Test
    public void withDupDetectorShouldCreateNewContextWithSameState() {
        // Arrange: Create a root context and a child object context.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext originalContext = new JsonReadContext(rootContext, 1, null,
                JsonStreamContext.TYPE_OBJECT, 2, 1);

        // Act: Create a new context by calling withDupDetector.
        // In this scenario, we are "replacing" a null detector with another null detector.
        JsonReadContext newContext = originalContext.withDupDetector(null);

        // Assert: Verify the new context is a distinct instance but has the same state.
        assertNotSame("withDupDetector should create a new instance.", originalContext, newContext);

        // Check that core properties are preserved from the original context.
        assertEquals("Type should be preserved", "OBJECT", newContext.getTypeDesc());
        assertEquals("Entry count should be preserved", 0, newContext.getEntryCount());
        assertEquals("Nesting depth should be preserved", 1, newContext.getNestingDepth());
        assertSame("Parent context should be the same", rootContext, newContext.getParent());

        // Verify the new DupDetector is set correctly.
        assertNull("The new DupDetector should be null as specified", newContext.getDupDetector());
    }
}