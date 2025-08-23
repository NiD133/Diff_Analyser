package com.google.common.graph;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link EndpointPair} focusing on specific method behaviors.
 */
@RunWith(JUnit4.class)
public class EndpointPairTest {

    /**
     * Tests that for an unordered self-loop edge, nodeV() returns the node itself.
     */
    @Test
    public void nodeV_ofUnorderedSelfLoop_returnsSameNode() {
        // Arrange: Create an unordered EndpointPair representing a self-loop,
        // where both endpoints are the same node.
        String node = "A";
        EndpointPair<String> selfLoopPair = EndpointPair.unordered(node, node);

        // Act: Retrieve the 'V' node from the pair. In a self-loop, this should
        // be the same as the 'U' node.
        String retrievedNodeV = selfLoopPair.nodeV();

        // Assert: The retrieved node must be the exact same instance as the original node.
        assertSame("For a self-loop, nodeV() should return the node itself.", node, retrievedNodeV);
    }
}