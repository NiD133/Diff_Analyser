package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link EndpointPair}.
 */
@RunWith(JUnit4.class)
public class EndpointPairTest {

    @Test
    public void of_forUndirectedGraph_returnsUnorderedPair() {
        // Arrange: Create an undirected graph, as EndpointPair.of() uses its properties
        // to determine the type of pair to create.
        Graph<Integer> undirectedGraph = GraphBuilder.undirected().build();
        Integer nodeU = 1;
        Integer nodeV = 2;

        // Act: Create an EndpointPair using the factory method for a generic graph.
        EndpointPair<Integer> endpointPair = EndpointPair.of(undirectedGraph, nodeU, nodeV);

        // Assert: Verify that the factory method produced the correct type of EndpointPair.
        // For an undirected graph, the resulting pair should be unordered.
        assertFalse(
            "EndpointPair for an undirected graph should be unordered", endpointPair.isOrdered());

        // The created pair should be equal to an explicitly created unordered pair.
        // This is a robust check, as it relies on the EndpointPair's own equals() logic,
        // which correctly handles that the internal order of nodes is not guaranteed.
        assertEquals(EndpointPair.unordered(nodeU, nodeV), endpointPair);
    }
}