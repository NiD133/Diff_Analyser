package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link JsonWriteContext}.
 */
public class JsonWriteContextTest extends JUnit5TestBase {
    // [core#1421]: Verify custom context extension works
    @Test
    void extendedContextCanBeCreatedAndInitialized() {
        // Local subclass to test protected constructor accessibility
        class TestJsonWriteContext extends JsonWriteContext {
            TestJsonWriteContext(int type, JsonWriteContext parent, DupDetector dups, Object currValue) {
                super(type, parent, dups, currValue);
            }
        }

        final int rootType = JsonStreamContext.TYPE_ROOT;
        final Object testValue = 0;
        
        // Create extended context instance
        JsonWriteContext context = new TestJsonWriteContext(rootType, null, null, testValue);

        // Verify basic initialization
        assertNotNull(context, "Context instance should not be null");
        assertTrue(context.inRoot(), "Context should be root type");
        assertNull(context.getParent(), "Root context should not have parent");
        assertEquals(testValue, context.getCurrentValue(), 
            "Current value should match constructor argument");
    }
}