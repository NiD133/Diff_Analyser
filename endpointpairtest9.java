package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EndpointPairTestTest9 {

    private static final Integer N0 = 0;
    private static final Integer N1 = 1;
    private static final Integer N2 = 2;
    private static final Integer N3 = 3;
    private static final Integer N4 = 4;

    private static final String E12 = "1-2";
    private static final String E12_A = "1-2a"; // Parallel edge to E12
    private static final String E13 = "1-3";
    private static final String E44 = "4-4"; // Self-loop edge

    @Test
    public void asGraph_onUndirectedNetwork_edgesAreUnorderedEndpointPairs() {
        // Arrange
        MutableNetwork<Integer, String> network =
                NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();

        // Add nodes and edges to cover different cases
        network.addNode(N0); // An isolated node
        network.addEdge(N1, N2, E12);
        network.addEdge(N2, N1, E12_A); // A parallel edge
        network.addEdge(N1, N3, E13);   // A standard edge
        network.addEdge(N4, N4, E44);   // A self-loop edge

        // Act
        Set<EndpointPair<Integer>> graphEdges = network.asGraph().edges();

        // Assert
        // The asGraph().edges() view should contain one EndpointPair for each connection.
        // For an undirected graph, these are unordered pairs, and parallel edges are collapsed
        // into a single EndpointPair.
        assertThat(graphEdges)
                .containsExactly(
                        EndpointPair.unordered(N1, N2), // The parallel edges (E12, E12_A) become one pair
                        EndpointPair.unordered(N1, N3),
                        EndpointPair.unordered(N4, N4));
    }
}