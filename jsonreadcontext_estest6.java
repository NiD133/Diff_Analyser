package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test class contains the refactored test case.
 * The original scaffolding and class name are kept for context.
 */
public class JsonReadContext_ESTestTest6 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that withDupDetector() creates a new context instance
     * while preserving the properties of the original context.
     */
    @Test
    public void withDupDetector_shouldCreateNewContextWithPropertiesPreserved() {
        // --- Arrange ---
        // 1. Create a root duplicate detector and a root context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);

        // 2. Create a child context representing a JSON array.
        final int line = 1;
        final int col = 1;
        final int nestingDepth = 1;
        JsonReadContext childArrayContext = new JsonReadContext(rootContext, nestingDepth, dupDetector,
                JsonStreamContext.TYPE_ARRAY, line, col);

        // --- Act ---
        // 3. Create a new context by calling withDupDetector.
        //    This should return a new instance with the specified detector.
        JsonReadContext newContext = childArrayContext.withDupDetector(dupDetector);

        // --- Assert ---
        // 4. Verify that a new instance was created.
        assertNotSame("withDupDetector should create a new instance.", childArrayContext, newContext);

        // 5. Verify the new context has the same properties as the original child context.
        assertEquals("Nesting depth should be preserved.",
                childArrayContext.getNestingDepth(), newContext.getNestingDepth());
        assertEquals("Entry count should be preserved.",
                childArrayContext.getEntryCount(), newContext.getEntryCount());
        assertEquals("Context type (inArray) should be preserved.",
                childArrayContext.inArray(), newContext.inArray());
        assertTrue("New context should be an array context.", newContext.inArray());

        // 6. As a sanity check, ensure the original root context was not modified.
        assertEquals("Root context nesting depth should remain 0.", 0, rootContext.getNestingDepth());
        assertFalse("Root context should not be an array context.", rootContext.inArray());
    }
}