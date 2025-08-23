package com.google.common.graph;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for {@link EndpointPair}.
 *
 * <p>Note: The original test class name and structure (e.g., EndpointPair_ESTestTest30)
 * suggested it was auto-generated. This version uses standard, human-readable conventions.
 */
public class EndpointPairTest {

    /**
     * Tests that the iterator for an unordered self-loop pair returns the node twice.
     * A self-loop is an edge that connects a node to itself.
     */
    @Test
    public void iterator_onUnorderedSelfLoop_returnsNodeTwice() {
        // Arrange: Create an unordered EndpointPair representing a self-loop (node -> node).
        Integer node = -1;
        EndpointPair<Integer> selfLoopPair = EndpointPair.unordered(node, node);

        // Act: Consume the iterator to get the sequence of nodes.
        List<Integer> iteratedNodes = ImmutableList.copyOf(selfLoopPair.iterator());

        // Assert: The iterator should yield the node twice.
        List<Integer> expectedNodes = ImmutableList.of(node, node);
        assertEquals(expectedNodes, iteratedNodes);
    }
}