package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;
import org.junit.Test;

/**
 * Tests for {@link EndpointPair}, focusing on its behavior within an undirected {@link Graph}.
 */
public class EndpointPairTest {

    private static final Integer NODE_1 = 1;
    private static final Integer NODE_2 = 2;
    private static final Integer NODE_3 = 3;
    private static final Integer NODE_4 = 4;

    @Test
    public void edges_onUndirectedGraph_returnsUnorderedEndpointPairs() {
        // ARRANGE: Create an undirected graph with a standard edge, a self-loop,
        // and a redundant edge to verify set-like behavior.
        MutableGraph<Integer> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();

        graph.putEdge(NODE_1, NODE_2);
        graph.putEdge(NODE_1, NODE_3);
        graph.putEdge(NODE_4, NODE_4); // A self-loop edge

        // In an undirected graph, edge {1, 2} is the same as {2, 1}.
        // This call should not add a new edge.
        graph.putEdge(NODE_2, NODE_1);

        // ACT: Retrieve the set of edges from the graph.
        Set<EndpointPair<Integer>> edges = graph.edges();

        // ASSERT: The set should contain the correct unordered endpoint pairs,
        // ignoring the redundant edge.
        assertThat(edges)
            .containsExactly(
                EndpointPair.unordered(NODE_1, NODE_2),
                EndpointPair.unordered(NODE_1, NODE_3),
                EndpointPair.unordered(NODE_4, NODE_4));
    }
}