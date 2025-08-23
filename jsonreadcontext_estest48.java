package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test class is a placeholder for the original's name.
 * In a real-world scenario, this would be named something like `JsonReadContextTest`.
 */
public class JsonReadContext_ESTestTest48 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that a root JsonReadContext is correctly initialized with a DupDetector.
     * It verifies that the initial state, such as nesting depth, entry count, and type,
     * is set as expected for a root context.
     */
    @Test
    public void whenRootContextIsCreatedWithDupDetector_thenInitialStateIsCorrect() {
        // Arrange: Create a duplicate detector for the context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        int lineNr = 1;
        int colNr = 1;

        // Act: Create a new root context.
        JsonReadContext rootContext = JsonReadContext.createRootContext(lineNr, colNr, dupDetector);

        // Assert: Verify the properties of the newly created root context.
        // Check that the correct DupDetector instance is associated.
        assertSame("The provided DupDetector instance should be set in the context.",
                dupDetector, rootContext.getDupDetector());

        // Check the standard properties of a root context.
        assertEquals("A root context should have a nesting depth of 0.",
                0, rootContext.getNestingDepth());
        assertEquals("A new context should have an entry count of 0.",
                0, rootContext.getEntryCount());
        assertEquals("The type description for a root context should be 'ROOT'.",
                "ROOT", rootContext.getTypeDesc());
    }
}