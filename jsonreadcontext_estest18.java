package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.json.JsonReadContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the behavior of the JsonReadContext,
 * particularly its parent-child relationships and state management.
 */
public class JsonReadContext_ESTestTest18 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that getParent() correctly returns the parent context from a nested structure
     * and that the properties of all contexts in the chain are as expected.
     */
    @Test
    public void getParent_fromNestedContext_shouldReturnCorrectParent() {
        // Arrange: Create a nested context structure: root -> object -> array
        // The DupDetector is not relevant for this test and is set to null.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext objectContext = rootContext.createChildObjectContext(2, 1); // line 2, col 1
        JsonReadContext arrayContext = objectContext.createChildArrayContext(2, 0);  // line 2, col 0

        // Act: Get the parent of the innermost (array) context.
        JsonReadContext retrievedParent = arrayContext.getParent();

        // Assert: Verify the returned parent is the expected object context.
        assertNotNull("The parent context should not be null", retrievedParent);
        assertSame("The retrieved parent should be the same instance as the object context",
                objectContext, retrievedParent);

        // Assertions for the parent context (the object context)
        assertEquals("Parent's type description should be 'OBJECT'", "OBJECT", retrievedParent.getTypeDesc());
        assertEquals("Parent's nesting depth should be 1 (one level below root)", 1, retrievedParent.getNestingDepth());
        assertEquals("Parent's initial entry count should be 0", 0, retrievedParent.getEntryCount());

        // Assertions for the original child context (the array context)
        assertEquals("Child's nesting depth should be 2", 2, arrayContext.getNestingDepth());
        assertTrue("Child context should be in an array", arrayContext.inArray());
        assertFalse("Child context should NOT be in an object", arrayContext.inObject());

        // Assertions for the root context
        assertEquals("Root's type description should be 'ROOT'", "ROOT", rootContext.getTypeDesc());
        assertEquals("Root's initial entry count should be 0", 0, rootContext.getEntryCount());
    }
}