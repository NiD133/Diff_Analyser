package com.google.common.graph;

import org.junit.Test;

/**
 * Unit tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    /**
     * Tests that calling EndpointPair.of() with a null Network instance
     * throws a NullPointerException, as the method contract requires a non-null network.
     */
    @Test(expected = NullPointerException.class)
    public void of_network_whenNetworkIsNull_throwsNullPointerException() {
        // Arrange: Define two nodes for the endpoint pair.
        String nodeU = "A";
        String nodeV = "B";

        // Act: Attempt to create an EndpointPair with a null network.
        // The cast to Network<?, ?> is necessary to resolve method ambiguity.
        EndpointPair.of((Network<?, ?>) null, nodeU, nodeV);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}