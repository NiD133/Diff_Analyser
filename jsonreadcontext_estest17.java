package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.json.JsonReadContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its initial state.
 */
public class JsonReadContextTest {

    /**
     * Verifies that a newly created root context has the correct initial properties,
     * most importantly that its parent is null.
     */
    @Test
    public void rootContextShouldHaveNullParentAndCorrectInitialState() {
        // Arrange: Create a root-level context.
        // The DupDetector is not relevant for this test, so it's set to null.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);

        // Act: Attempt to retrieve the parent of the root context.
        JsonReadContext parentContext = rootContext.getParent();

        // Assert: Verify the state of the root context.
        assertNull("The parent of a root context must be null.", parentContext);
        assertEquals("A root context should have a nesting depth of 0.", 0, rootContext.getNestingDepth());
        assertEquals("A new root context should have an entry count of 0.", 0, rootContext.getEntryCount());
        assertEquals("The type description for a root context should be 'ROOT'.", "ROOT", rootContext.getTypeDesc());
    }
}