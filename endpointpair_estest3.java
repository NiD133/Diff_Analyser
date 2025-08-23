package com.google.common.graph;

import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void target_forOrderedSelfLoop_returnsSameNode() {
        // Arrange: Create an ordered EndpointPair representing a self-loop,
        // where the source and target are the same node.
        String node = "A";
        EndpointPair<String> selfLoopPair = EndpointPair.ordered(node, node);

        // Act: Retrieve the target node from the pair.
        String targetNode = selfLoopPair.target();

        // Assert: The retrieved target node should be the exact same instance
        // as the original node.
        assertSame("The target of a self-loop should be the node itself.", node, targetNode);
    }
}