package com.google.common.graph;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork}.
 */
public class ImmutableNetworkTest {

    @Test
    public void copyOf_whenCalledOnImmutableNetwork_returnsSameInstance() {
        // Arrange: Create an initial immutable network. Its contents are not important for this test.
        ImmutableNetwork<Integer, Integer> originalNetwork =
            ImmutableNetwork.copyOf(NetworkBuilder.directed().<Integer, Integer>build());

        // Act: Call copyOf() on the existing immutable network instance.
        ImmutableNetwork<Integer, Integer> networkCopy = ImmutableNetwork.copyOf(originalNetwork);

        // Assert: The method should return the exact same instance, not a new one.
        // This is a common and documented optimization for immutable collections.
        assertSame(
            "copyOf(ImmutableNetwork) should be an identity operation, returning the same instance.",
            originalNetwork,
            networkCopy);
    }
}