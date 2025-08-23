package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ImmutableNetwork.Builder}. */
@RunWith(JUnit4.class)
public class ImmutableNetworkBuilderTest {

    private static final String NODE_A = "A";

    @Test
    public void addNode_createsNetworkWithNodeAndNoEdges() {
        // Arrange: Create a builder for a directed network.
        ImmutableNetwork.Builder<String, Integer> builder = NetworkBuilder.directed().immutable();

        // Act: Add a single node and build the network.
        ImmutableNetwork<String, Integer> network = builder.addNode(NODE_A).build();

        // Assert: The network should contain the added node and no edges.
        assertThat(network.nodes()).containsExactly(NODE_A);
        assertThat(network.edges()).isEmpty();
    }
}