package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link JsonWriteContext}.
 * 
 * These tests verify the behavior and extensibility of JsonWriteContext,
 * which is used internally by Jackson to track the current state during
 * JSON writing operations (e.g., whether we're in an object, array, or root context).
 */
public class JsonWriteContextTest extends JUnit5TestBase
{
    /**
     * Test helper class that extends JsonWriteContext to verify that
     * the class can be properly extended by subclasses.
     * 
     * This is important for Jackson's extensibility model where users
     * might need to create custom context implementations.
     */
    static class TestableJsonWriteContext extends JsonWriteContext {
        
        /**
         * Creates a testable JsonWriteContext with the specified parameters.
         * 
         * @param contextType The type of context (root, object, array)
         * @param parentContext The parent context (null for root)
         * @param duplicateDetector Optional duplicate field name detector
         * @param currentValue The current value being processed
         */
        public TestableJsonWriteContext(int contextType, JsonWriteContext parentContext, 
                                      DupDetector duplicateDetector, Object currentValue) {
            super(contextType, parentContext, duplicateDetector, currentValue);
        }
    }

    /**
     * Test for issue #1421: Verifies that JsonWriteContext can be extended
     * and instantiated properly by subclasses.
     * 
     * This test ensures that:
     * 1. Custom subclasses of JsonWriteContext can be created
     * 2. The constructor properly initializes the context
     * 3. The resulting instance is valid and not null
     */
    @Test
    void testJsonWriteContextCanBeExtendedBySubclasses() {
        // Given: Parameters for creating a root context
        int rootContextType = 0;  // Root context type
        JsonWriteContext noParentContext = null;  // Root has no parent
        DupDetector noDuplicateDetection = null;  // No duplicate detection needed
        Object initialValue = Integer.valueOf(0);  // Simple test value
        
        // When: Creating a custom JsonWriteContext subclass instance
        TestableJsonWriteContext customContext = new TestableJsonWriteContext(
            rootContextType, 
            noParentContext, 
            noDuplicateDetection, 
            initialValue
        );
        
        // Then: The context should be successfully created
        assertNotNull(customContext, 
            "Custom JsonWriteContext subclass should be instantiated successfully");
    }
}