package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork#asGraph()}.
 */
public class ImmutableNetworkTest {

    @Test
    public void asGraph_forEmptyNetwork_returnsEmptyGraph() {
        // Arrange: Create an empty immutable network. Using the builder is the
        // standard and most readable way to construct an ImmutableNetwork.
        ImmutableNetwork<Integer, String> emptyNetwork =
            NetworkBuilder.directed().<Integer, String>immutable().build();

        // Act: Get the graph representation of the network.
        Graph<Integer> graphView = emptyNetwork.asGraph();

        // Assert: The resulting graph should be non-null, empty, and share the
        // same properties as the original network.
        assertNotNull("The graph view should not be null", graphView);
        assertTrue("The graph view of an empty network should have no nodes", graphView.nodes().isEmpty());
        assertTrue("The graph view of an empty network should have no edges", graphView.edges().isEmpty());
        assertEquals(
            "The graph's directedness should match the network's",
            emptyNetwork.isDirected(),
            graphView.isDirected());
    }
}