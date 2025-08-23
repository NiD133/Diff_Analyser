package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on context state management.
 */
public class JsonWriteContextTest {

    /**
     * Tests that creating a child context and then calling {@link JsonWriteContext#clearAndGetParent()}
     * on it does not alter the state (entry count and current index) of the parent context.
     */
    @Test
    public void clearAndGetParentOnChildShouldNotAffectParentState() {
        // Arrange: Create a root context and write two values to it, establishing a known state.
        // A child context is created between the writes to ensure its lifecycle doesn't interfere.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.writeValue(); // Simulates writing a first value. entryCount=1, currentIndex=0

        JsonWriteContext childContext = rootContext.createChildObjectContext();

        rootContext.writeValue(); // Simulates writing a second value. entryCount=2, currentIndex=1

        // Act: Clear the child context. This is the operation under test.
        JsonStreamContext returnedParent = childContext.clearAndGetParent();

        // Assert: Verify the parent context's state remains unchanged and the correct parent is returned.
        final int expectedIndex = 1;
        final int expectedEntryCount = 2;

        assertSame("clearAndGetParent() should return the direct parent context", rootContext, returnedParent);
        assertEquals("Parent's current index should be unaffected", expectedIndex, rootContext.getCurrentIndex());
        assertEquals("Parent's entry count should be unaffected", expectedEntryCount, rootContext.getEntryCount());
    }
}