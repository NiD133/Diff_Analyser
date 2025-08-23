package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link EndpointPair} class, focusing on its factory methods.
 */
public class EndpointPairTest {

    /**
     * Tests that creating an EndpointPair for an undirected graph with two identical nodes
     * results in a valid, unordered, self-loop pair.
     */
    @Test
    public void of_forUndirectedGraph_withIdenticalNodes_createsSelfLoopPair() {
        // Arrange: Create an undirected graph and define a single node for a self-loop. [2, 16]
        MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().build(); [2, 16]
        Integer node = 0;

        // Act: Create an EndpointPair using the static factory 'of' for the graph and nodes.
        EndpointPair<Integer> selfLoopPair = EndpointPair.of(undirectedGraph, node, node);

        // Assert: Verify that the created pair is a non-null, unordered self-loop.
        assertNotNull("The created EndpointPair should not be null.", selfLoopPair);
        assertFalse("The pair should be unordered for an undirected graph.", selfLoopPair.isOrdered()); [1]
        assertEquals("nodeU() should be the provided node.", node, selfLoopPair.nodeU()); [1]
        assertEquals("nodeV() should be the provided node, forming a self-loop.", node, selfLoopPair.nodeV()); [1, 9]
    }
}