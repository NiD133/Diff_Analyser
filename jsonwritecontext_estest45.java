package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link JsonWriteContext} class, focusing on child context creation.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that creating a child object context from a root context
     * correctly initializes the child's state (type, depth, parent)
     * and leaves the root context's state unchanged.
     */
    @Test
    public void createChildObjectContext_fromRoot_shouldInitializeChildCorrectly() {
        // Arrange: Create a root-level write context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);

        // Act: Create a new child context for a JSON object.
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext();

        // Assert: Verify the state of the newly created child context.
        assertNotNull("The created child context should not be null.", childObjectContext);
        assertEquals("Child context type should be 'OBJECT'.", "OBJECT", childObjectContext.getTypeDesc());
        assertEquals("Child nesting depth should be 1 (one level below root).", 1, childObjectContext.getNestingDepth());
        assertEquals("A new child context should have an entry count of 0.", 0, childObjectContext.getEntryCount());
        assertSame("The parent of the child context should be the root context.", rootContext, childObjectContext.getParent());

        // Assert: Verify the root context remains in its initial state.
        assertEquals("Root context entry count should remain 0.", 0, rootContext.getEntryCount());
    }
}