package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that `clearAndGetParent()` correctly returns the parent context
     * from a nested structure without altering the state of other contexts.
     * This simulates closing an object within an array, like `[ { ... } ]`.
     */
    @Test
    public void clearAndGetParent_whenCalledOnNestedContext_shouldReturnParent() {
        // Arrange: Create a nested context structure: root -> array -> object
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 1, null);
        JsonReadContext arrayContext = rootContext.createChildArrayContext(2, 1);
        JsonReadContext objectContext = arrayContext.createChildObjectContext(2, 3);

        // Pre-conditions: Verify initial state
        assertEquals("Root context should have nesting depth 0.", 0, rootContext.getNestingDepth());
        assertEquals("Array context should have nesting depth 1.", 1, arrayContext.getNestingDepth());
        assertEquals("Object context should have nesting depth 2.", 2, objectContext.getNestingDepth());
        assertTrue("The innermost context should be an object.", objectContext.inObject());

        // Act: Call the method under test on the innermost context
        JsonReadContext parentContext = objectContext.clearAndGetParent();

        // Assert
        // 1. The returned context should be the direct parent (the array context).
        assertNotNull("The returned parent context should not be null.", parentContext);
        assertSame("Should return the exact same instance as the direct parent.", arrayContext, parentContext);

        // 2. The state of the returned parent context should be correct.
        assertTrue("The parent context should be an array.", parentContext.inArray());
        assertEquals("The parent's entry count should be unaffected.", 0, parentContext.getEntryCount());

        // 3. The state of the original (child) context should remain inspectable.
        assertTrue("The original context should still identify as an object.", objectContext.inObject());
        assertEquals("The original context's nesting depth should be unchanged.", 2, objectContext.getNestingDepth());
        
        // 4. The root context should be unaffected.
        assertEquals("root", rootContext.typeDesc());
        assertEquals("Root context nesting depth should remain 0.", 0, rootContext.getNestingDepth());
    }
}