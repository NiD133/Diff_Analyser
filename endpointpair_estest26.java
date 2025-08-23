package com.google.common.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void adjacentNode_onUnorderedSelfLoop_returnsNodeItself() {
        // Arrange: Create an unordered endpoint pair representing a self-loop edge from "A" to "A".
        String node = "A";
        EndpointPair<String> selfLoopPair = EndpointPair.unordered(node, node);

        // Act: Get the node adjacent to "A" in the self-loop.
        String adjacentNode = selfLoopPair.adjacentNode(node);

        // Assert: For a self-loop, the adjacent node should be the node itself.
        assertEquals(node, adjacentNode);
    }
}