package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on the
 * creation and initialization of child contexts.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that creating a child array context from a root context
     * correctly initializes the state of both the root and the new child context.
     */
    @Test
    public void whenChildArrayContextCreated_thenStateIsCorrectlyInitialized() {
        // Arrange: Create a root context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);

        // Act: Create a child array context from the root.
        // The original test used the DupDetector instance as the "current value" for the child context.
        JsonWriteContext childArrayContext = rootContext.createChildArrayContext(dupDetector);

        // Assert: Verify the state of both contexts.
        assertNotNull("The child array context should be successfully created.", childArrayContext);

        // 1. Verify the state of the parent (root) context is unchanged.
        assertTrue("The parent context should remain the root.", rootContext.inRoot());
        assertEquals("The parent context's entry count should still be 0.", 0, rootContext.getEntryCount());

        // 2. Verify the state of the new child context is correctly initialized.
        assertEquals("The child context's type should be 'ARRAY'.", "ARRAY", childArrayContext.getTypeDesc());
        assertEquals("The nesting depth should be 1 for a direct child.", 1, childArrayContext.getNestingDepth());
        assertEquals("The new child context should have an entry count of 0.", 0, childArrayContext.getEntryCount());
    }
}