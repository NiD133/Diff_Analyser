package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that creating a child array context correctly initializes its state,
     * particularly that it increments the parent's nesting depth.
     * This test uses an arbitrary negative nesting depth for the parent context
     * to ensure the increment logic is robust and not dependent on positive-only depths.
     */
    @Test
    public void createChildArrayContext_shouldIncrementParentNestingDepth() {
        // Arrange
        // Use an arbitrary negative depth to ensure the calculation is robust.
        final int parentNestingDepth = -2041;
        final int parentLine = 2;
        final int parentCol = -2260;
        final int childLine = -2041;
        final int childCol = 2;

        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 1, dupDetector);

        // Create a parent context (simulating an object) with the arbitrary nesting depth.
        JsonReadContext parentObjectContext = new JsonReadContext(rootContext,
                parentNestingDepth, dupDetector, JsonStreamContext.TYPE_OBJECT, parentLine, parentCol);

        // Act
        JsonReadContext childArrayContext = parentObjectContext.createChildArrayContext(childLine, childCol);

        // Assert
        // 1. Verify the state of the newly created child array context.
        assertEquals("Child context should be an array", "ARRAY", childArrayContext.getTypeDesc());
        assertEquals("Child nesting depth should be parent's depth + 1",
                parentNestingDepth + 1, childArrayContext.getNestingDepth());
        assertEquals("A new context should have an entry count of 0", 0, childArrayContext.getEntryCount());

        // 2. Verify the state of the original root context remains unchanged.
        assertEquals("Root context nesting depth should always be 0", 0, rootContext.getNestingDepth());
        assertFalse("Root context should not be considered 'in an array'", rootContext.inArray());
    }
}