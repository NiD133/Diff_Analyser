package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonReadContext_ESTestTest42 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that creating multiple child contexts from the same parent reuses the same
     * child context instance. This is an intentional optimization in JsonReadContext
     * to reduce object allocation.
     */
    @Test
    public void createChildContext_shouldReuseChildInstanceForEfficiency() {
        // Arrange: Create a root context, which will be the parent for our child contexts.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);

        // Act 1: Create the first child context (an object).
        JsonReadContext objectContext = rootContext.createChildObjectContext(1, 10);

        // Assert 1: Verify the state of the newly created object context.
        assertEquals("Object", objectContext.typeDesc());
        assertTrue(objectContext.inObject());
        assertEquals(1, objectContext.getNestingDepth());

        // Act 2: Create a second child context (an array) from the same root.
        // This should reuse and re-initialize the previous child instance.
        JsonReadContext arrayContext = rootContext.createChildArrayContext(2, 20);

        // Assert 2: Verify that the same instance was reused and its state was updated correctly.
        assertSame("For performance, the same child context instance should be reused.",
                objectContext, arrayContext);

        assertEquals("The type of the reused context should be updated to 'Array'.",
                "Array", arrayContext.typeDesc());
        assertTrue("The reused context should now be an array context.",
                arrayContext.inArray());
        assertEquals(0, arrayContext.getEntryCount());
        assertEquals(1, arrayContext.getNestingDepth());
    }
}