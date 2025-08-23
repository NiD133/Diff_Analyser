package com.google.common.graph;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test(expected = NullPointerException.class)
    public void of_network_withNullNodes_throwsNullPointerException() {
        // Arrange: Create a network instance. Its configuration is not important for this test.
        Network<String, Integer> network = NetworkBuilder.directed().build();

        // Act: Call the method under test with null arguments for the nodes.
        // According to the contract, this should trigger a Preconditions check.
        EndpointPair.of(network, null, null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the `expected` attribute of the @Test annotation.
    }
}