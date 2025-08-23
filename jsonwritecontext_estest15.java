package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonStreamContext;

/**
 * This test class contains the refactored test case.
 * The original class name `JsonWriteContext_ESTestTest15` and scaffolding are kept for context.
 */
public class JsonWriteContext_ESTestTest15 extends JsonWriteContext_ESTest_scaffolding {

    /**
     * Tests that a parent context's value index is correctly maintained
     * even after a child context is created and the parent is modified again.
     */
    @Test
    public void parentContextIndexShouldUpdateCorrectlyAfterChildCreation() {
        // Arrange: Create a root context, which behaves like a JSON array.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("Initially, the index should be -1", -1, rootContext.getCurrentIndex());

        // Act: Write the first value to the root context.
        rootContext.writeValue();
        assertEquals("After one value, the index should be 0", 0, rootContext.getCurrentIndex());

        // Arrange: Create a child context from the root.
        JsonWriteContext childContext = rootContext.createChildObjectContext();
        
        // Assert: Verify the parent-child relationship is established correctly.
        assertNotNull("Child context should be successfully created", childContext);
        assertSame("The parent of the child should be the root context", rootContext, childContext.getParent());

        // Act: Write a second value to the PARENT (root) context. This is the key
        // action being tested—modifying the parent while a child context exists.
        rootContext.writeValue();

        // Assert: The parent's index should be incremented, unaffected by the child's creation.
        assertEquals("After the second value, the parent index should be 1", 1, rootContext.getCurrentIndex());
        assertTrue("Parent context should report having a current index", rootContext.hasCurrentIndex());
    }
}