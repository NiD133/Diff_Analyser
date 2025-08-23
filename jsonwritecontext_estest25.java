package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on context management.
 */
public class JsonWriteContextTest {

    /**
     * Tests that calling clearAndGetParent() on a deeply nested context
     * successfully returns its direct parent with its state intact.
     */
    @Test
    public void clearAndGetParent_onNestedContext_returnsDirectParent() {
        // Arrange: Create a nested context structure: root -> array -> array
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext parentArrayContext = rootContext.createChildArrayContext();
        JsonWriteContext childArrayContext = parentArrayContext.createChildArrayContext();

        // Sanity check the initial state
        assertEquals(2, childArrayContext.getNestingDepth());
        assertTrue(rootContext.inRoot());

        // Act: Clear the innermost context and get its parent
        JsonWriteContext returnedParentContext = childArrayContext.clearAndGetParent();

        // Assert: Verify the returned object is the correct parent instance and its state is as expected.
        assertNotNull("The returned context should not be null", returnedParentContext);
        assertSame("Should return the exact same instance as the direct parent",
                parentArrayContext, returnedParentContext);

        // Verify the state of the returned parent context
        assertEquals("Parent's type should be 'Array'", "Array", returnedParentContext.typeDesc());
        assertEquals("Parent's nesting depth should be 1", 1, returnedParentContext.getNestingDepth());
        assertEquals("Parent's entry count should be 0, as no values were written",
                0, returnedParentContext.getEntryCount());

        // For completeness, confirm the original child context's immutable properties are unchanged.
        assertEquals("Original child's nesting depth should remain 2",
                2, childArrayContext.getNestingDepth());
    }
}