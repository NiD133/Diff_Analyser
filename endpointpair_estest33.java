package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;

// The class name and runner are kept to match the original test suite's structure.
// In a real-world scenario, the class name would also be improved for clarity.
@RunWith(EvoRunner.class)
public class EndpointPair_ESTestTest33 extends EndpointPair_ESTest_scaffolding {

    /**
     * Verifies that calling {@code incidentNodes()} on a network with an object
     * that is not an edge in the network throws an {@link IllegalArgumentException}.
     */
    @Test
    public void incidentNodes_whenEdgeIsNotInGraph_throwsIllegalArgumentException() {
        // Arrange: Create an empty network. The node and edge types are illustrative.
        MutableNetwork<String, Integer> network = NetworkBuilder.directed().build();

        // Create an EndpointPair to represent a potential edge. This object itself is valid,
        // but it has not been added as an edge to the network.
        EndpointPair<String> nonExistentEdge = EndpointPair.unordered("A", "A");

        // Act & Assert: Verify that the operation fails with a clear, descriptive error message.
        try {
            // The incidentNodes() method requires an edge object that is an element of the network.
            network.incidentNodes(nonExistentEdge);
            fail("Expected an IllegalArgumentException because the edge is not in the graph.");
        } catch (IllegalArgumentException e) {
            // The exception message should clearly state that the edge is not an element of the graph.
            String expectedMessage = "Edge " + nonExistentEdge + " is not an element of this graph.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}