package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on state management and parent-child relationships.
 */
public class JsonReadContextTest {

    /**
     * Tests that {@link JsonReadContext#clearAndGetParent()} correctly returns the parent context
     * and resets the internal state of the child context from which it was called.
     */
    @Test
    public void clearAndGetParent_shouldReturnParentAndResetChildState() {
        // Arrange: Set up a parent-child relationship between two JsonReadContext instances.
        
        // 1. Create a root context. The values are arbitrary but chosen to match the
        // original test's logic of using non-standard types and nesting depths.
        final int rootNestingDepth = -10;
        final int unknownTypeForRoot = 99;
        JsonReadContext rootContext = new JsonReadContext(
                null, rootNestingDepth, null, unknownTypeForRoot, 1, 10);

        // 2. Create a child context linked to the root.
        // This uses a deprecated constructor that automatically calculates nesting depth.
        final int unknownTypeForChild = -99;
        JsonReadContext childContext = new JsonReadContext(
                rootContext, null, unknownTypeForChild, 2, 5);

        // Pre-condition check: Verify the initial state of the child context.
        // Its nesting depth should be the parent's depth + 1.
        assertEquals("Initial nesting depth should be parent's depth + 1",
                rootNestingDepth + 1, childContext.getNestingDepth());
        assertEquals("Initial entry count should be 0", 0, childContext.getEntryCount());


        // Act: Call the method under test.
        JsonReadContext returnedParent = childContext.clearAndGetParent();


        // Assert: Verify the outcome.
        
        // 1. The method should return the original parent context instance.
        assertNotNull("Returned parent context should not be null", returnedParent);
        assertSame("The returned object should be the original parent context instance",
                rootContext, returnedParent);

        // 2. The child context's state should be reset.
        // Specifically, `clearAndGetParent` resets the internal index, making the entry count 0.
        assertEquals("Child's entry count should be reset to 0 after clearing",
                0, childContext.getEntryCount());

        // 3. Other properties of both child and parent should remain unchanged.
        assertEquals("Child's nesting depth should not change",
                rootNestingDepth + 1, childContext.getNestingDepth());
        assertEquals("Parent's entry count should be unchanged",
                0, returnedParent.getEntryCount());
        
        // The type description should remain '?' for the unknown type codes used.
        assertEquals("Child's type description should remain '?'",
                "?", childContext.getTypeDesc());
        assertEquals("Parent's type description should remain '?'",
                "?", returnedParent.getTypeDesc());
    }
}