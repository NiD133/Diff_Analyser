package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on its state management.
 */
public class JsonReadContextTest {

    /**
     * Verifies that a value set on a root context via {@link JsonReadContext#setCurrentValue(Object)}
     * can be correctly retrieved using {@link JsonReadContext#getCurrentValue()}.
     * <p>
     * This test also confirms that setting a value does not alter the context's
     * fundamental properties, such as its nesting depth or root status.
     */
    @Test
    public void shouldSetAndGetCurrentValueOnRootContext() {
        // Arrange: Create a root-level JsonReadContext.
        // A DupDetector is required for context creation but is not the focus of this test.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 0, dupDetector);
        final String testValue = "a-simple-test-value";

        // Act: Set a value on the context and then retrieve it.
        rootContext.setCurrentValue(testValue);
        Object retrievedValue = rootContext.getCurrentValue();

        // Assert: Verify the retrieved value and the context's state.
        assertEquals("The retrieved value should match the value that was set.",
                testValue, retrievedValue);

        // Verify that the core properties of the root context are correct.
        assertTrue("A newly created root context should be identified as 'in root'.",
                rootContext.inRoot());
        assertEquals("The nesting depth of a root context should be 0.",
                0, rootContext.getNestingDepth());
        assertEquals("The entry count for a new root context should be 0.",
                0, rootContext.getEntryCount());
    }
}