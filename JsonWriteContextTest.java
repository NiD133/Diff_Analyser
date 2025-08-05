package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.DupDetector;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link JsonWriteContext}.
 */
class JsonWriteContextTest {

    /**
     * A helper class that extends {@link JsonWriteContext} to gain access to its protected constructor for testing purposes.
     * This allows direct instantiation of a {@link JsonWriteContext} subclass.
     */
    static class TestJsonWriteContext extends JsonWriteContext {
        public TestJsonWriteContext(int type, JsonWriteContext parent, DupDetector dups, Object currValue) {
            super(type, parent, dups, currValue);
        }
    }

    /**
     * Tests the ability to subclass JsonWriteContext and successfully instantiate it.
     * This verifies that the protected constructor is accessible to subclasses as intended.
     */
    @Test
    void shouldAllowSubclassInstantiation() {
        // GIVEN: A value for the current context
        final Object currentValue = new Object();

        // WHEN: A subclass of JsonWriteContext is instantiated as a root context
        TestJsonWriteContext context = new TestJsonWriteContext(JsonStreamContext.TYPE_ROOT, null, null, currentValue);

        // THEN: The context object should be correctly initialized
        assertNotNull(context, "The context should not be null.");
        assertEquals(JsonStreamContext.TYPE_ROOT, context.getType(), "The context type should be TYPE_ROOT.");
        assertNull(context.getParent(), "The parent of a root context should be null.");
        assertSame(currentValue, context.getCurrentValue(), "The current value should be set correctly.");
    }
}