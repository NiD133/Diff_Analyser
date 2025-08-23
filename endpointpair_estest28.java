package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the interaction between EndpointPair and the Network class.
 */
public class EndpointPairInteractionTest {

    /**
     * Verifies that querying for the incident nodes of an edge that is not in the network
     * throws an IllegalArgumentException.
     */
    @Test
    public void incidentNodes_whenEdgeNotInNetwork_throwsIllegalArgumentException() {
        // Arrange: Create a network and an EndpointPair representing an edge.
        // In this setup, the EndpointPair itself is used as the edge object.
        MutableNetwork<String, EndpointPair<String>> network = NetworkBuilder.undirected().build();

        // This EndpointPair represents an edge that has NOT been added to the network.
        EndpointPair<String> nonExistentEdge = EndpointPair.ordered("A", "B");

        // Act & Assert: Verify that the expected exception is thrown.
        try {
            network.incidentNodes(nonExistentEdge);
            fail("Expected an IllegalArgumentException for an edge not present in the network.");
        } catch (IllegalArgumentException expected) {
            // The exception message should clearly state that the edge is not in the graph.
            // The message uses the toString() representation of the EndpointPair.
            String expectedMessage = "Edge <A -> B> is not an element of this graph.";
            assertEquals(expectedMessage, expected.getMessage());
        }
    }
}