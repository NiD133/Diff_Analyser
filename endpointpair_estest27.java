package com.google.common.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void adjacentNode_forOrderedPair_shouldReturnOppositeNode() {
        // Arrange: Create an ordered pair of two distinct nodes.
        String source = "U";
        String target = "V";
        EndpointPair<String> orderedPair = EndpointPair.ordered(source, target);

        // Act: Get the node adjacent to the target node.
        String adjacentNode = orderedPair.adjacentNode(target);

        // Assert: The adjacent node to the target should be the source.
        assertEquals("The node adjacent to the target should be the source.", source, adjacentNode);
    }
}