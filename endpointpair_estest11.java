package com.google.common.graph;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test(expected = NullPointerException.class)
    public void of_graph_whenGraphIsNull_throwsNullPointerException() {
        // Arrange: Define two arbitrary nodes for the endpoint pair.
        String nodeU = "A";
        String nodeV = "B";

        // Act: Call the method under test with a null graph.
        // The @Test(expected=...) annotation will handle the assertion.
        EndpointPair.of(null, nodeU, nodeV);
    }
}