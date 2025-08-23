package com.google.common.graph;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork}.
 */
public class ImmutableNetworkTest {

    @Test
    public void copyOf_onImmutableNetwork_returnsSameInstance() {
        // Arrange: Create an initial ImmutableNetwork.
        // The specific contents of the network are not important for this test.
        ImmutableNetwork<Object, Object> originalNetwork =
            ImmutableNetwork.copyOf(NetworkBuilder.directed().build());

        // Act: Call copyOf() on the already-immutable network.
        ImmutableNetwork<Object, Object> newNetwork = ImmutableNetwork.copyOf(originalNetwork);

        // Assert: The method should return the exact same instance as an optimization,
        // rather than creating a new copy.
        assertSame("copyOf(ImmutableNetwork) should return the same instance", originalNetwork, newNetwork);
    }
}