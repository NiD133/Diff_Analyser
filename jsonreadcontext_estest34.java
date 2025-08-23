package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

// Note: The original test class name and inheritance are preserved as per the prompt.
public class JsonReadContext_ESTestTest34 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that calling `clearAndGetParent()` on a child context does not alter the state
     * (like entry count) of the parent context.
     */
    @Test
    public void clearAndGetParentOnChildShouldNotAffectParentState() {
        // Arrange: Create a root context and simulate reading two elements to advance its state.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(191, 191, dupDetector);

        // Simulate parsing two elements in the root context. Each call to expectComma()
        // after the first value increments the internal entry count.
        rootContext.expectComma(); // After first element, entry count is 1
        rootContext.expectComma(); // After second element, entry count is 2

        // Create a child context from the root.
        JsonReadContext childContext = rootContext.createChildArrayContext(-1, 191);

        // Act: Clear the child context. This operation is meant to be called when a
        // JSON array/object scope ends. It should not modify the parent's state.
        childContext.clearAndGetParent();

        // Assert: Verify that the parent context's state remains unchanged.
        assertEquals("Parent's entry count should be unaffected by the child's lifecycle.",
                2, rootContext.getEntryCount());
        assertEquals("Parent's type description should remain 'ROOT'.",
                "ROOT", rootContext.getTypeDesc());
    }
}