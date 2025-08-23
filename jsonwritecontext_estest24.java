package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on context
 * management and state transitions.
 */
public class JsonWriteContextTest {

    /**
     * Tests that calling `clearAndGetParent()` on a nested context correctly
     * returns the parent context and resets the state of the child context.
     */
    @Test
    public void clearAndGetParent_whenCalledOnNestedContext_shouldReturnParentAndResetChildState() {
        // Arrange: Create a nested context structure: root -> object -> array
        // This simulates the state when writing JSON like `{ "key": [ ...`
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        Object arrayContextValue = new Object();
        JsonWriteContext arrayContext = objectContext.createChildArrayContext(arrayContextValue);

        // Pre-conditions to verify our setup is correct
        assertEquals("Initial nesting depth should be 2", 2, arrayContext.getNestingDepth());
        assertTrue(arrayContext.inArray());
        assertTrue(objectContext.inObject());
        assertTrue(rootContext.inRoot());

        // Act: Clear the innermost (array) context and retrieve its parent
        JsonWriteContext parentContext = arrayContext.clearAndGetParent();

        // Assert: Verify the state of the contexts after the operation

        // 1. The returned context should be the correct parent instance.
        assertNotNull("Parent context should not be null", parentContext);
        assertSame("Should return the direct parent context", objectContext, parentContext);

        // 2. The parent context's state should be unchanged.
        assertTrue("Parent should still be in an object context", parentContext.inObject());
        assertEquals("Parent's entry count should be unchanged", 0, parentContext.getEntryCount());

        // 3. The child context (on which the method was called) should be reset.
        //    Its structural properties like type and depth remain, but its content state is cleared.
        assertEquals("Nesting depth of cleared context remains", 2, arrayContext.getNestingDepth());
        assertEquals("Entry count of cleared context should be reset to 0", 0, arrayContext.getEntryCount());
        assertTrue("Cleared context should still identify as an array", arrayContext.inArray());
    }
}