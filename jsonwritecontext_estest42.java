package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on context creation and parent-child relationships.
 * Note: The original test class name "JsonWriteContext_ESTestTest42" was generated and has been omitted for clarity.
 */
public class JsonWriteContextTest {

    /**
     * Tests that getParent() correctly returns the direct parent context
     * from a nested structure (root -> array -> object).
     */
    @Test
    public void getParentShouldReturnCorrectParentContextForNestedObject() {
        // Arrange: Create a nested context structure: root -> array -> object
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        JsonWriteContext objectContext = arrayContext.createChildObjectContext(new Object());

        // Act: Retrieve the parent of the innermost (object) context
        JsonWriteContext parentOfObjectContext = objectContext.getParent();

        // Assert: Verify the retrieved parent and the state of the contexts

        // 1. Check that the retrieved parent is the correct instance
        assertNotNull("Parent context should not be null", parentOfObjectContext);
        assertSame("getParent() should return the exact parent instance", arrayContext, parentOfObjectContext);

        // 2. Verify properties of the parent (the array context)
        assertTrue("Parent context should be of type array", parentOfObjectContext.inArray());
        assertEquals("Entry count of the parent array context should be 0", 0, parentOfObjectContext.getEntryCount());

        // 3. Verify properties of the child (the object context)
        assertEquals("Nesting depth of the object context should be 2", 2, objectContext.getNestingDepth());
        assertEquals("Entry count of the object context should be 0", 0, objectContext.getEntryCount());
        assertEquals("The type description should be 'Object'", "Object", objectContext.typeDesc());

        // 4. Verify a property of the root context for completeness
        assertFalse("Root context should not be in an array", rootContext.inArray());
    }
}