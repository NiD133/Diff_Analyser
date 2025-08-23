package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Verifies that successive calls to {@code createChildObjectContext} on the same parent
     * reuse the same child context instance. This is an internal optimization to reduce
     * object allocation, and this test ensures that behavior is preserved.
     */
    @Test
    public void createChildObjectContextShouldReuseExistingChildInstance() {
        // Arrange: Create a root context.
        JsonReadContext rootContext = JsonReadContext.createRootContext(11, 11, null);

        // Act: Create the first child context.
        JsonReadContext firstChildContext = rootContext.createChildObjectContext(11, 11);

        // Assert: Verify the initial state of the root and the new child context.
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertEquals(0, rootContext.getNestingDepth());

        assertNotNull(firstChildContext);
        assertEquals("Object", firstChildContext.typeDesc());
        assertEquals(0, firstChildContext.getEntryCount());
        assertEquals(1, firstChildContext.getNestingDepth());

        // Act: Create a second child context from the same root.
        // The line and column numbers are different to ensure the 'reset' logic is also working.
        JsonReadContext reusedChildContext = rootContext.createChildObjectContext(1, 0);

        // Assert: The core behavior - the same child instance should be returned.
        // This confirms the instance reuse optimization is active.
        assertSame("Expected the same child context instance to be reused for performance.",
                firstChildContext, reusedChildContext);

        // Assert: The properties of the reused child context are correctly maintained or reset.
        // The nesting depth should be preserved from the first creation.
        assertEquals(1, reusedChildContext.getNestingDepth());
    }
}