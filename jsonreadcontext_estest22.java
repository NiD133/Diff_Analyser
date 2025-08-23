package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonReadContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link JsonReadContext} focusing on parent-child relationships.
 */
public class JsonReadContextTest {

    /**
     * Verifies that calling getParent() on a child array context correctly returns
     * the original root context with its expected properties.
     */
    @Test
    public void getParent_fromChildArrayContext_shouldReturnRootContext() {
        // Arrange: Create a root context and a child array context from it.
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 1, null);
        JsonReadContext childArrayContext = rootContext.createChildArrayContext(2, 5);

        // Act: Retrieve the parent of the child context.
        JsonReadContext parentContext = childArrayContext.getParent();

        // Assert: Verify the retrieved parent is the original root context.
        assertNotNull("Parent context should not be null", parentContext);
        assertSame("The retrieved parent should be the same instance as the root context",
                rootContext, parentContext);

        // Assert: Verify properties of the child context.
        assertTrue("Child context should be in an array", childArrayContext.inArray());
        assertEquals("Child context should have a nesting depth of 1", 1, childArrayContext.getNestingDepth());

        // Assert: Verify properties of the parent (root) context.
        assertFalse("Root context should not be in an array", parentContext.inArray());
        assertEquals("Root context should have a nesting depth of 0", 0, parentContext.getNestingDepth());
        assertEquals("Root context should have an entry count of 0", 0, parentContext.getEntryCount());
    }
}