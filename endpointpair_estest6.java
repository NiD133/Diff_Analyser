package com.google.common.graph;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void isOrdered_forPairFromUndirectedGraph_returnsFalse() {
        // Arrange: Create an EndpointPair from an undirected graph. The factory method
        // EndpointPair.of() should create an unordered pair in this context.
        Graph<String> undirectedGraph = GraphBuilder.undirected().build();
        EndpointPair<String> endpointPair = EndpointPair.of(undirectedGraph, "A", "B");

        // Act: Check if the pair is ordered.
        boolean isOrdered = endpointPair.isOrdered();

        // Assert: The pair should be unordered because it came from an undirected graph.
        assertFalse("EndpointPair from an undirected graph should not be ordered", isOrdered);
    }
}