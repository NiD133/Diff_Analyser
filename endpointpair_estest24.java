package com.google.common.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void of_directedNetwork_createsEqualOrderedPair() {
        // Arrange
        Integer node = -1068;
        Network<Object, Object> directedNetwork = NetworkBuilder.directed().build();

        // An EndpointPair created explicitly as ordered for a self-loop.
        EndpointPair<Object> explicitlyOrderedPair = EndpointPair.ordered(node, node);

        // Act
        // An EndpointPair created from a directed network, which should also be ordered.
        EndpointPair<Object> pairFromDirectedNetwork = EndpointPair.of(directedNetwork, node, node);

        // Assert
        // The two pairs should be equal because they are both ordered and have the same
        // source and target nodes.
        assertEquals(explicitlyOrderedPair, pairFromDirectedNetwork);
    }
}