package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its initial state.
 */
public class JsonReadContextTest {

    /**
     * Verifies that a root-level JsonReadContext is initialized with the correct
     * default properties upon creation.
     */
    @Test
    public void createRootContext_shouldInitializeStateCorrectly() {
        // Arrange: Define location and a duplicate detector for the context.
        final int lineNr = 1;
        final int colNr = 10;
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);

        // Act: Create a new root context.
        JsonReadContext rootContext = JsonReadContext.createRootContext(lineNr, colNr, dupDetector);

        // Assert: Verify the initial state of the newly created context.
        // The context should correctly identify itself as the root.
        assertEquals("Type description should be 'ROOT'", "ROOT", rootContext.getTypeDesc());
        assertEquals("Nesting depth of root should be 0", 0, rootContext.getNestingDepth());
        assertEquals("Initial entry count should be 0", 0, rootContext.getEntryCount());
        assertNull("Parent of a root context should be null", rootContext.getParent());

        // It should hold the provided location information.
        assertEquals("Initial line number should be set correctly", lineNr, rootContext.getLineNr());
        assertEquals("Initial column number should be set correctly", colNr, rootContext.getColumnNr());

        // And its content-related properties should be null by default.
        assertNull("Current value should be null initially", rootContext.getCurrentValue());
        assertNull("Current name should be null initially", rootContext.getCurrentName());
    }
}