package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the behavior of {@link EndpointPair} within the context of a {@link Graph},
 * specifically for the {@link Graph#edges()} set.
 */
@RunWith(JUnit4.class)
public class EndpointPairInGraphTest {

    private static final Integer N1 = 1;
    private static final Integer N2 = 2;
    private static final Integer N3 = 3;
    private static final Integer N4 = 4;

    /**
     * Verifies that the `edges()` set of a directed graph contains the correct `EndpointPair`
     * instances and correctly handles lookups for various other pair types.
     */
    @Test
    public void edges_ofDirectedGraph_containsCorrectlyOrderedPairs() {
        // ARRANGE: Create a directed graph with a self-loop and a regular edge.
        MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
        directedGraph.putEdge(N1, N1);
        directedGraph.putEdge(N1, N2);

        // ACT: Retrieve the set of edges.
        Set<EndpointPair<Integer>> edges = directedGraph.edges();

        // ASSERT: The set should contain exactly the two ordered pairs for the added edges.
        assertThat(edges)
            .containsExactly(EndpointPair.ordered(N1, N1), EndpointPair.ordered(N1, N2));

        // ASSERT: The set should NOT contain pairs that don't match the graph's state.
        // An unordered pair is never equal to an ordered one.
        assertThat(edges).doesNotContain(EndpointPair.unordered(N1, N2));
        // The reverse of a directed edge should not be present.
        assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N1));
        // An edge that was not added should not be present.
        assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N2));
        // An edge with nodes not in the graph should not be present.
        assertThat(edges).doesNotContain(EndpointPair.ordered(N3, N4));
    }
}