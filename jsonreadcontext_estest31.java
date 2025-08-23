package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the JsonReadContext.
 * The original test class name is preserved for context.
 */
public class JsonReadContext_ESTestTest31 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Verifies that calling clearAndGetParent() on a child context
     * does not modify the state (e.g., the current name) of its parent context.
     */
    @Test
    public void clearAndGetParentOnChildShouldNotAffectParentState() throws JsonProcessingException {
        // Arrange: Create a root context, set its current name, and then create a child context.
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 1, null);
        final String fieldName = "testField";
        rootContext.setCurrentName(fieldName);
        
        JsonReadContext childContext = rootContext.createChildObjectContext(2, 5);

        // Act: Clear the child context. The method under test is clearAndGetParent().
        childContext.clearAndGetParent();

        // Assert: The parent context's state should remain unchanged.
        assertTrue("Parent context should still have its current name after the child is cleared.",
                rootContext.hasCurrentName());
        assertEquals("The parent's current name should be unaffected.",
                fieldName, rootContext.getCurrentName());

        // This assertion from the original test confirms the child context's identity.
        assertFalse("The child context should not be identified as the root context.",
                childContext.inRoot());
    }
}