package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonWriteContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test verifies the instance reuse optimization within JsonWriteContext.
 * When a child context is requested from a parent, a new child is created.
 * If another child context is requested from the same parent, the previously
 * created child instance is reused and reset with the new type and state.
 */
public class JsonWriteContextTest {

    @Test
    public void createChildContext_shouldReuseAndResetExistingChildInstance() {
        // Arrange: Create a root context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act 1: Create the first child context, which should be an object context.
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext();

        // Assert 1: Verify the initial state of the newly created child context.
        assertNotNull("The first child context should not be null.", childObjectContext);
        assertTrue("The child context should initially be an object context.", childObjectContext.inObject());
        assertEquals("Nesting depth for a direct child should be 1.", 1, childObjectContext.getNestingDepth());

        // Arrange 2: Prepare a value for the next context creation.
        Object newContextValue = "a-new-value";

        // Act 2: Create a second child from the same root. This should reuse the previous instance
        // and reset it to be an array context.
        JsonWriteContext reusedChildAsArrayContext = rootContext.createChildArrayContext(newContextValue);

        // Assert 2: Verify that the same instance was reused and its state was correctly updated.
        assertSame("The same child context instance should be reused.",
                childObjectContext, reusedChildAsArrayContext);

        assertFalse("The reused context should no longer be an object context.",
                reusedChildAsArrayContext.inObject());
        assertTrue("The reused context should now be an array context.",
                reusedChildAsArrayContext.inArray());
        assertEquals("The nesting depth should remain 1.", 1, reusedChildAsArrayContext.getNestingDepth());
        assertEquals("The current value should have been updated.",
                newContextValue, reusedChildAsArrayContext.getCurrentValue());
    }
}