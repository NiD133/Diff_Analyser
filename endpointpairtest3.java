package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link EndpointPair} class.
 */
public class EndpointPairTest {

    /**
     * Verifies that the target() method of an ordered EndpointPair
     * correctly returns the node specified as the target during creation.
     */
    @Test
    public void orderedPair_target_returnsCorrectNode() {
        // Arrange: Define source and target nodes for a directed edge.
        String sourceNode = "A";
        String targetNode = "B";
        EndpointPair<String> endpointPair = EndpointPair.ordered(sourceNode, targetNode);

        // Act: Retrieve the target node from the endpoint pair.
        String actualTarget = endpointPair.target();

        // Assert: The retrieved target node should be the same as the one provided.
        assertSame("The target() method should return the node provided as the target.",
                targetNode, actualTarget);
    }

    /**
     * Verifies that the target() method of an ordered EndpointPair
     * returns the correct node when the edge is a self-loop.
     */
    @Test
    public void orderedPair_target_returnsCorrectNodeOnSelfLoop() {
        // Arrange: Create a single node for a self-looping edge.
        String node = "LOOP";
        EndpointPair<String> selfLoopPair = EndpointPair.ordered(node, node);

        // Act: Retrieve the target node from the self-looping pair.
        String actualTarget = selfLoopPair.target();

        // Assert: The target of a self-loop should be the node itself.
        assertSame("The target of a self-loop should be the node itself.", node, actualTarget);
    }
}