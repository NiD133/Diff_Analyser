package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest6 {

    /**
     * Tests that an ImmutableNetwork.Builder copies the configuration from the NetworkBuilder
     * it was created from, and is not affected by later changes to that NetworkBuilder.
     */
    @Test
    @SuppressWarnings("CheckReturnValue")
    public void immutableBuilder_copiesConfiguration_andIsUnaffectedByLaterChanges() {
        // Arrange: Create a NetworkBuilder with a specific initial configuration.
        NetworkBuilder<String, Integer> originalBuilder =
            NetworkBuilder.directed()
                .allowsSelfLoops(true)
                .nodeOrder(ElementOrder.<String>natural());

        // Act: Create an ImmutableNetwork.Builder from the original. This should be a snapshot
        // of the original builder's configuration at this moment.
        ImmutableNetwork.Builder<String, Integer> immutableBuilder = originalBuilder.immutable();

        // Act: Modify the original builder. These changes should NOT affect the immutable builder.
        originalBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.unordered());

        // Build the network from the immutable builder.
        ImmutableNetwork<String, Integer> network = immutableBuilder.build();

        // Assert: The final network has the configuration that was copied, not the modified one.
        assertThat(network.isDirected()).isTrue();
        assertThat(network.allowsSelfLoops()).isTrue(); // Should be true, not the updated 'false'.
        assertThat(network.nodeOrder()).isEqualTo(ElementOrder.natural()); // Should be natural, not the updated 'unordered'.
    }
}