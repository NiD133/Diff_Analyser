package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that the withDupDetector() method creates a new context instance
     * with the specified duplicate detector, while preserving all other properties
     * from the original context.
     */
    @Test
    public void withDupDetector_shouldReturnNewContextWithReplacedDetector() {
        // Arrange
        // 1. Create two distinct duplicate detectors to swap between.
        DupDetector originalDetector = DupDetector.rootDetector((JsonParser) null);
        DupDetector newDetector = originalDetector.child();

        // 2. Create an initial JsonReadContext with the original detector.
        JsonReadContext parentContext = JsonReadContext.createRootContext(null);
        final int nestingDepth = 5;
        final int type = JsonReadContext.TYPE_OBJECT;
        final int lineNr = 10;
        final int colNr = 20;
        
        JsonReadContext originalContext = new JsonReadContext(parentContext, nestingDepth, originalDetector, type, lineNr, colNr);

        // Act
        // Create a new context by replacing the duplicate detector.
        JsonReadContext newContext = originalContext.withDupDetector(newDetector);

        // Assert
        // 1. A new instance should be created, not a modification of the original.
        assertNotSame("withDupDetector should create a new instance.", originalContext, newContext);

        // 2. The duplicate detector should be updated in the new context.
        assertSame("The new context should have the new detector.", newDetector, newContext.getDupDetector());
        assertNotSame("The new context's detector should be different from the original's.",
                originalContext.getDupDetector(), newContext.getDupDetector());

        // 3. All other properties should be copied from the original context.
        assertSame("Parent context should be preserved.",
                originalContext.getParent(), newContext.getParent());
        assertEquals("Nesting depth should be copied.",
                originalContext.getNestingDepth(), newContext.getNestingDepth());
        assertEquals("Context type should be copied.",
                originalContext.getTypeDesc(), newContext.getTypeDesc());
        assertEquals("Entry count should be the same (initially zero).",
                originalContext.getEntryCount(), newContext.getEntryCount());
    }
}