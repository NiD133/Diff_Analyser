package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on context hierarchy and state.
 */
public class JsonReadContextTest {

    /**
     * Tests that calling getParent() on a nested context returns the correct immediate parent
     * and that both the child and parent contexts have the correct state (type and nesting depth).
     */
    @Test
    public void getParent_whenCalledOnNestedObjectContext_shouldReturnParentArrayContext() {
        // Arrange: Create a nested context structure: root -> array -> object
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 1, null);
        JsonReadContext arrayContext = rootContext.createChildArrayContext(2, 5);
        JsonReadContext objectContext = arrayContext.createChildObjectContext(3, 10);

        // Act: Retrieve the parent of the innermost (object) context.
        JsonReadContext parentContext = objectContext.getParent();

        // Assert: Verify the returned parent is the correct instance and states are valid.

        // 1. The most important check: the returned parent is the exact same object as the array context.
        assertSame("getParent() should return the direct parent context instance.", arrayContext, parentContext);

        // 2. Verify the state of the original child context (the object).
        assertEquals("The child context should be of type OBJECT.", "OBJECT", objectContext.getTypeDesc());
        assertEquals("The child context's nesting depth should be 2 (root=0, array=1, object=2).", 2, objectContext.getNestingDepth());

        // 3. Verify the state of the retrieved parent context (the array).
        assertEquals("The parent context's nesting depth should be 1 (root=0, array=1).", 1, parentContext.getNestingDepth());
        assertEquals("The parent context should have an initial entry count of 0.", 0, parentContext.getEntryCount());
    }
}