package com.google.common.graph;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork.Builder}.
 */
public class ImmutableNetworkBuilderTest {

    @Test
    public void addEdge_returnsSameBuilderInstanceForChaining() {
        // Arrange: Create a builder and define the nodes and edge to be added.
        // The public API for creating an ImmutableNetwork.Builder is via NetworkBuilder.
        ImmutableNetwork.Builder<Integer, String> builder =
                NetworkBuilder.directed().<Integer, String>immutable();
        Integer sourceNode = 1;
        Integer targetNode = 2;
        String edge = "E1";

        // Act: Add an edge and capture the returned builder instance.
        // The addEdge method should return the same builder instance to allow for fluent method chaining.
        ImmutableNetwork.Builder<Integer, String> returnedBuilder =
                builder.addEdge(sourceNode, targetNode, edge);

        // Assert:
        // 1. Verify that the method supports the fluent builder pattern by returning itself.
        // This was the implicit goal of the original test's assertNotNull check.
        assertSame(
                "addEdge() should return the same builder instance for method chaining.",
                builder,
                returnedBuilder);

        // 2. Verify the state of the builder by building the network. This makes the test
        // more robust by confirming the edge was actually added.
        ImmutableNetwork<Integer, String> network = builder.build();
        assertTrue("The built network should contain the newly added edge.", network.edges().contains(edge));
        assertTrue(
                "The built network should have an edge connecting the source and target nodes.",
                network.hasEdgeConnecting(sourceNode, targetNode));
    }
}