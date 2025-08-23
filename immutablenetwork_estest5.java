package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork}.
 */
public class ImmutableNetwork_ESTestTest5 { // Retaining original class name as requested

    /**
     * Tests that building a network from a builder creates a new instance,
     * distinct from any network the builder may have been based on.
     */
    @Test
    public void build_fromBuilderBasedOnExistingNetwork_createsNewInstance() {
        // Arrange: Create an initial empty immutable network.
        ImmutableNetwork<Integer, Integer> emptyNetwork = NetworkBuilder.directed()
                .<Integer, Integer>immutable()
                .build();

        // Arrange: Create a builder from the empty network and prepare a new node.
        ImmutableNetwork.Builder<Integer, Integer> builder = NetworkBuilder.from(emptyNetwork).immutable();
        Integer node = -967;

        // Act: Add the node to the builder and build a new network.
        ImmutableNetwork<Integer, Integer> networkWithNode = builder.addNode(node).build();

        // Assert: The new network is a different instance from the original.
        assertNotSame("Building should create a new network instance", emptyNetwork, networkWithNode);

        // Assert: The original network remains unchanged (and empty).
        assertTrue("Original network should remain empty", emptyNetwork.nodes().isEmpty());

        // Assert: The new network correctly contains the added node.
        assertEquals("New network should have one node", 1, networkWithNode.nodes().size());
        assertTrue("New network should contain the added node", networkWithNode.nodes().contains(node));
    }
}