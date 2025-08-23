package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonReadContext_ESTestTest19 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that a child context correctly references its parent and has the proper nesting depth,
     * even when the parent's state is modified after the child's creation.
     */
    @Test
    public void childContextShouldHaveCorrectParentAndNestingDepth() throws JsonProcessingException {
        // Arrange: Create a root context and a child object context from it.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(1, 1, dupDetector);
        JsonReadContext childObjectContext = rootContext.createChildObjectContext(2, 5);

        // Act: Set a property (the current name) on the parent context *after* the child has been created.
        rootContext.setCurrentName("rootFieldName");

        // Assert: Verify the properties of both the child and parent contexts.

        // 1. Verify child context properties are correctly initialized and independent.
        assertEquals("Child's nesting depth should be one level deeper than the root.", 1, childObjectContext.getNestingDepth());
        assertSame("Child's parent should be the original root context.", rootContext, childObjectContext.getParent());
        assertFalse("Child context should not have a current name.", childObjectContext.hasCurrentName());

        // 2. Verify parent context state was updated correctly.
        assertTrue("Parent context should have a current name after it was set.", rootContext.hasCurrentName());
        assertEquals("Parent context should retain the name that was set.", "rootFieldName", rootContext.getCurrentName());
    }
}