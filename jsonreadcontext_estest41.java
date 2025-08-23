package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the instance reuse behavior of JsonReadContext.
 */
public class JsonReadContextTest {

    /**
     * Tests that successive calls to create a child context from the same parent
     * will reuse and reset the same child context instance, rather than allocating a new one.
     * This is an important performance optimization.
     */
    @Test
    public void createChildObjectContext_shouldReuseChildInstanceOnSubsequentCalls() {
        // Arrange: Create a root context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);

        // Act: Create the first child object context.
        JsonReadContext firstChildContext = rootContext.createChildObjectContext(1, 10);

        // Assert: Verify the initial state of the newly created child context.
        assertNotNull("The first child context should not be null.", firstChildContext);
        assertTrue("Context should be an object.", firstChildContext.inObject());
        assertEquals("Nesting depth should be 1.", 1, firstChildContext.getNestingDepth());
        assertEquals("Initial entry count should be 0.", 0, firstChildContext.getEntryCount());

        // Act: Create a second child object context from the same root.
        JsonReadContext secondChildContext = rootContext.createChildObjectContext(5, 20);

        // Assert: The core behavior - the same instance should be reused.
        assertSame("The same child context instance should be reused for the second call.",
                firstChildContext, secondChildContext);

        // Assert: The state of the reused context should be consistent.
        // The internal `reset()` method is called, so properties like entry count are reset.
        assertEquals("Nesting depth of the reused context should still be 1.", 1, secondChildContext.getNestingDepth());
        assertEquals("Type description should remain 'Object'.", "Object", secondChildContext.typeDesc());

        // Assert: The root context's state remains unchanged.
        assertEquals("Root context nesting depth should remain 0.", 0, rootContext.getNestingDepth());
        assertEquals("Root context type description should be 'ROOT'.", "ROOT", rootContext.getTypeDesc());
    }
}