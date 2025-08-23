package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import java.util.Collections;
import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork.Builder}.
 */
public class ImmutableNetworkBuilderTest {

    @Test
    public void build_withSingleNode_createsNetworkContainingOnlyThatNode() {
        // Arrange
        Integer node = -967;

        // Act: Create an ImmutableNetwork with a single node using the builder.
        ImmutableNetwork<Integer, Integer> network =
            NetworkBuilder.directed()
                .<Integer, Integer>immutable()
                .addNode(node)
                .build();

        // Assert: Verify the network contains exactly the one node that was added.
        assertEquals(Collections.singleton(node), network.nodes());
    }
}