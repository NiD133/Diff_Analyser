package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on its instance reuse mechanism.
 */
public class JsonReadContext_ESTestTest28 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests the instance reuse mechanism of JsonReadContext.
     * <p>
     * This test verifies that when a pre-existing context instance is recycled to become a
     * child context, its type and index are correctly reset. It also highlights a specific
     * behavior: the nesting depth is NOT updated upon reuse. The recycled instance retains
     * its original nesting depth.
     */
    @Test
    public void createChildArrayContext_whenReusingInstance_resetsTypeButNotNestingDepth() {
        // Arrange: Set up a parent context and a pre-existing context instance
        // that will be recycled as its child.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);

        // 1. Create a parent context.
        JsonReadContext parentContext = JsonReadContext.createRootContext(-2391, 1, dupDetector);

        // 2. Create a separate root context instance that will be recycled.
        //    As a root context, its initial nesting depth is 0.
        JsonReadContext contextToRecycle = JsonReadContext.createRootContext(dupDetector);
        assertEquals("Initial type should be ROOT", "ROOT", contextToRecycle.getTypeDesc());

        // 3. Manually set the `_child` field to enable instance recycling.
        //    This simulates the internal state where a child context is available for reuse.
        parentContext._child = contextToRecycle;

        // Act: Create a child array context. This action should recycle the `contextToRecycle`
        // instance instead of creating a new one.
        JsonReadContext childContext = parentContext.createChildArrayContext(547, -2391);

        // Assert: Verify the state of the recycled context.
        // The same instance should be returned.
        assertSame("The recycled instance should be the same object as the new child",
                contextToRecycle, childContext);

        // The type and index should be reset for the new array context.
        assertEquals("Type should be reset to ARRAY", "ARRAY", childContext.getTypeDesc());
        assertEquals("Current index should be reset to 0", 0, childContext.getCurrentIndex());

        // The parent context's state should remain unchanged.
        assertEquals("Parent nesting depth should remain 0", 0, parentContext.getNestingDepth());

        // This is the key assertion: The `reset()` method, called during recycling,
        // does not update the nesting depth. The child context retains the
        // nesting depth (0) from its previous state as a root context,
        // instead of being updated to `parent.nestingDepth + 1`.
        assertEquals("Recycled child's nesting depth should NOT be updated",
                0, childContext.getNestingDepth());
    }
}