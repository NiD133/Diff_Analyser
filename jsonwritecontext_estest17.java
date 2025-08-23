package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on its state management.
 */
public class JsonWriteContextTest {

    /**
     * Tests that a value can be set and retrieved on a root context,
     * and that this operation does not alter other fundamental context properties
     * like nesting depth or type.
     */
    @Test
    public void shouldSetAndGetCurrentValueOnRootContext() {
        // Arrange: Create a root context and a value to associate with it.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object testValue = new Object();

        // Act: Set the current value on the context and then retrieve it.
        rootContext.setCurrentValue(testValue);
        Object retrievedValue = rootContext.getCurrentValue();

        // Assert
        // 1. Verify that the current value was set and can be retrieved correctly.
        assertSame("The retrieved value should be the same instance that was set.",
                testValue, retrievedValue);

        // 2. Verify that setting the current value does not affect other context properties.
        assertEquals("Nesting depth should remain 0 for the root context.",
                0, rootContext.getNestingDepth());
        assertEquals("Entry count should remain 0 for a new root context.",
                0, rootContext.getEntryCount());
        assertEquals("Type description should remain 'ROOT' for the root context.",
                "ROOT", rootContext.getTypeDesc());
    }
}