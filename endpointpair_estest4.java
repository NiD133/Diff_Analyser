package com.google.common.graph;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    /**
     * Tests that for an unordered self-loop edge (where both nodes are the same),
     * nodeV() returns the correct node instance.
     */
    @Test
    public void unorderedPair_selfLoop_nodeV_returnsSameNode() {
        // Arrange: Create an unordered EndpointPair representing a self-loop edge on a single node.
        String node = "A";
        EndpointPair<String> selfLoopPair = EndpointPair.unordered(node, node);

        // Act: Get the 'V' node from the pair.
        String nodeV = selfLoopPair.nodeV();

        // Assert: The returned node should be the same instance as the original node.
        assertSame("For a self-loop, nodeV() should return the node itself.", node, nodeV);
    }
}