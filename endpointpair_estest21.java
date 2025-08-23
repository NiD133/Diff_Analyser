package com.google.common.graph;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the equality logic of {@link EndpointPair}.
 */
public class EndpointPairEqualityTest {

    /**
     * Verifies that an ordered EndpointPair is not equal to an unordered one,
     * even when they contain the same nodes.
     */
    @Test
    public void orderedPair_isNotEqualToUnorderedPair_withSameNodes() {
        // Arrange: Create an ordered and an unordered pair with the same node.
        // This represents a directed self-loop and an undirected self-loop.
        String node = "A";
        EndpointPair<String> orderedPair = EndpointPair.ordered(node, node);
        EndpointPair<String> unorderedPair = EndpointPair.unordered(node, node);

        // Act & Assert: Verify that the two pairs are not considered equal.
        // According to the EndpointPair contract, an ordered pair can never be equal
        // to an unordered pair, as they represent fundamentally different relationships
        // (directed vs. undirected).
        assertNotEquals(orderedPair, unorderedPair);
    }
}