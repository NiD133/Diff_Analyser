package com.google.common.graph;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test(expected = NullPointerException.class)
    public void of_graph_withNullNodes_throwsNullPointerException() {
        // Arrange: Create a graph instance. Its specific configuration is not
        // relevant for this test, as we are testing the null-handling of the node parameters.
        Graph<Object> graph = GraphBuilder.directed().build();

        // Act: Call the factory method with null arguments for both nodes.
        EndpointPair.of(graph, null, null);

        // Assert: The test is expected to throw a NullPointerException. This is
        // verified by the `expected` parameter of the @Test annotation.
    }
}