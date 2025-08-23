package com.google.common.graph;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    /**
     * Verifies that an EndpointPair created from an undirected graph
     * is correctly identified as unordered.
     */
    @Test
    public void isOrdered_whenCreatedFromUndirectedGraph_shouldReturnFalse() {
        // Arrange: Create a builder for an undirected graph. The factory method
        // EndpointPair.of() uses the graph's properties to determine orderedness.
        Graph<Integer> undirectedGraph = GraphBuilder.undirected().build(); [9, 10]
        Integer node = 0;

        // Act: Create an EndpointPair using the undirected graph.
        EndpointPair<Integer> endpointPair = EndpointPair.of(undirectedGraph, node, node);

        // Assert: The resulting EndpointPair should not be ordered. [6]
        assertFalse("EndpointPair from an undirected graph should be unordered", endpointPair.isOrdered());
    }
}