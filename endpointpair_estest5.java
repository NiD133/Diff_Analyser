package com.google.common.graph;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void of_forDirectedGraph_returnsOrderedPair() {
        // Arrange: Create a directed graph to provide context for the EndpointPair.
        // The nodes don't need to actually exist in the graph.
        Graph<String> directedGraph = GraphBuilder.directed().build();
        String source = "A";
        String target = "B";

        // Act: Create an EndpointPair using the directed graph.
        EndpointPair<String> endpointPair = EndpointPair.of(directedGraph, source, target);

        // Assert: The pair should be ordered because the graph is directed.
        assertTrue(
            "EndpointPair.of() for a directed graph should return an ordered pair",
            endpointPair.isOrdered());
    }
}