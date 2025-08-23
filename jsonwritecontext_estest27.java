package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.DupDetector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext#clearAndGetParent()} method.
 */
public class JsonWriteContextTest {

    /**
     * Tests that clearAndGetParent() correctly returns the parent context
     * and clears the current value of the child context.
     */
    @Test
    public void clearAndGetParent_shouldReturnParentAndClearCurrentValue() {
        // Arrange: Create a 3-level deep context hierarchy: root -> child -> grandchild
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // The child context uses a non-standard type to verify getTypeDesc() behavior.
        final int nonStandardContextType = 65599;
        JsonWriteContext childContext = new JsonWriteContext(nonStandardContextType, rootContext,
                (DupDetector) null, new Object());

        JsonWriteContext grandchildContext = new JsonWriteContext(5, childContext, (DupDetector) null);

        // Set a value on the grandchild to verify it gets cleared.
        Object grandchildsCurrentValue = new Object();
        grandchildContext.setCurrentValue(grandchildsCurrentValue);
        
        // Pre-Act Assertions: Verify the initial state before the call
        assertEquals("The parent of the grandchild should be the child context",
                childContext, grandchildContext.getParent());
        assertNotNull("The grandchild's current value should be set before clearing",
                grandchildContext.getCurrentValue());
        assertEquals("Grandchild nesting depth should be 2", 2, grandchildContext.getNestingDepth());

        // Act: Call the method under test
        JsonWriteContext returnedParent = grandchildContext.clearAndGetParent();

        // Assert: Verify the results of the operation
        
        // 1. Assertions about the returned context
        assertNotNull("The method should return a non-null parent context", returnedParent);
        assertSame("The returned context should be the direct parent (childContext)",
                childContext, returnedParent);

        // 2. Assertions about the state of the original context (grandchildContext)
        assertNull("The grandchild's current value should be cleared (set to null)",
                grandchildContext.getCurrentValue());
        assertEquals("The nesting depth of the grandchild should remain unchanged",
                2, grandchildContext.getNestingDepth());
        assertFalse("The grandchild context is not the root context", grandchildContext.inRoot());

        // 3. Assertions about the parent context's state (should be unchanged)
        assertEquals("ROOT", rootContext.getTypeDesc());
        // For non-standard types (not ROOT, ARRAY, or OBJECT), getTypeDesc() returns "?"
        assertEquals("?", returnedParent.getTypeDesc());
    }
}