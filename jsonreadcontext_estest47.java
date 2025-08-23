package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on its behavior
 * as a node in a parsing context tree.
 */
public class JsonReadContextTest {

    /**
     * Verifies that calling {@link JsonReadContext#clearAndGetParent()} on a root context
     * correctly returns null, as a root context has no parent.
     */
    @Test
    public void clearAndGetParent_whenCalledOnRootContext_shouldReturnNull() {
        // Arrange: Create a root-level JsonReadContext.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);

        // Act: Attempt to get the parent of the root context.
        JsonReadContext parentContext = rootContext.clearAndGetParent();

        // Assert:
        // The primary expectation is that a root context has no parent.
        assertNull("The parent of a root context should be null.", parentContext);

        // Also, verify that the state of the root context itself remains valid and unchanged.
        assertEquals("Type description should remain 'ROOT'", "ROOT", rootContext.getTypeDesc());
        assertEquals("Nesting depth should remain 0", 0, rootContext.getNestingDepth());
        assertEquals("Entry count should remain 0", 0, rootContext.getEntryCount());
    }
}