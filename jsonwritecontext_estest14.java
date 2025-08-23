package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the JsonWriteContext class, focusing on context creation and parent-child relationships.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that creating a child object context from a root context
     * correctly initializes the child's state (type, depth) and maintains
     * the correct link back to the parent.
     */
    @Test
    public void createChildObjectContext_shouldSetCorrectStateAndParent() {
        // Arrange: Create a root context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act: Create a child object context from the root.
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext();

        // Assert: Verify the properties of the newly created child context.
        assertEquals("Child context type should be OBJECT", "OBJECT", childObjectContext.getTypeDesc());
        assertEquals("Child context should be at nesting depth 1", 1, childObjectContext.getNestingDepth());

        // Assert: Verify the relationship with the parent context.
        JsonWriteContext parentContext = childObjectContext.getParent();
        assertSame("getParent() should return the original root context instance", rootContext, parentContext);
        assertTrue("The parent context should be the root", parentContext.inRoot());
        assertEquals("Creating a child should not affect the parent's entry count", 0, parentContext.getEntryCount());
    }
}