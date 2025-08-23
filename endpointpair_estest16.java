package com.google.common.graph;

import static com.google.common.graph.EndpointPair.unordered;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the equality logic in {@link EndpointPair}.
 */
public class EndpointPairEqualityTest {

    @Test
    public void equals_withNestedPairAsNode_returnsFalse() {
        // Arrange
        // Create a simple self-loop pair using a string node.
        String nodeA = "A";
        EndpointPair<String> selfLoopPair = unordered(nodeA, nodeA);

        // Create a second, different pair where one of its nodes is the first pair.
        // This tests that an EndpointPair is not equal to another EndpointPair
        // that contains the first one as a node.
        EndpointPair<Object> nestedPair = unordered(selfLoopPair, nodeA);

        // Act & Assert
        // The two pairs should not be equal because their sets of nodes are different.
        // selfLoopPair contains the nodes {"A", "A"}.
        // nestedPair contains the nodes {<EndpointPair {"A", "A"}>, "A"}.
        assertNotEquals(selfLoopPair, nestedPair);
    }
}