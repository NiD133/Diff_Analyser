package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class ImmutableNetwork_ESTestTest4 {

    /**
     * Tests that calling build() on a builder, which was created from an existing ImmutableNetwork,
     * produces a new and distinct network instance.
     */
    @Test
    public void build_fromBuilderOfExistingNetwork_createsNewInstance() {
        // Arrange: Create an initial, empty immutable network.
        ImmutableNetwork<Integer, String> initialNetwork =
                NetworkBuilder.undirected().<Integer, String>immutable().build();

        // Act: Create a new network by adding a node to a builder based on the initial network.
        ImmutableNetwork.Builder<Integer, String> builder = NetworkBuilder.from(initialNetwork).immutable();
        Integer node = 1;
        builder.addNode(node);
        ImmutableNetwork<Integer, String> newNetwork = builder.build();

        // Assert: The new network should be a different instance and contain the new node.
        assertNotSame("The new network should be a different instance", initialNetwork, newNetwork);
        assertThat(newNetwork.nodes()).contains(node);
        assertThat(initialNetwork.nodes()).isEmpty();
    }
}