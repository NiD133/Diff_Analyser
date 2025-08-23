package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on the state of a root context.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that a newly created root context has the expected initial state:
     * <ul>
     *     <li>It is correctly identified as a root context.</li>
     *     <li>Its parent is null.</li>
     *     <li>Its nesting depth and entry count are zero.</li>
     * </ul>
     */
    @Test
    public void createRootContext_shouldHaveNullParentAndZeroDepth() {
        // Arrange: Create a new root context.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act: Attempt to retrieve the parent of the root context.
        JsonWriteContext parentContext = rootContext.getParent();

        // Assert: Verify the initial state of the root context.
        assertTrue("A newly created context should be identified as the root", rootContext.inRoot());
        assertNull("The parent of a root context should be null", parentContext);
        assertEquals("The nesting depth of a root context should be 0", 0, rootContext.getNestingDepth());
        assertEquals("The initial entry count of a root context should be 0", 0, rootContext.getEntryCount());
    }
}