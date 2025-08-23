package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the live, unmodifiable view returned by {@link Graph#edges()}.
 */
@RunWith(JUnit4.class)
public class GraphEdgesTest {

    private static final Integer NODE_1 = 1;
    private static final Integer NODE_2 = 2;

    @Test
    public void edges_returnsLiveUnmodifiableView() {
        // Arrange
        MutableGraph<Integer> graph = GraphBuilder.directed().build();
        Set<EndpointPair<Integer>> edgesView = graph.edges();

        // The view should be empty initially
        assertThat(edgesView).isEmpty();

        // Act: Add an edge to the graph
        // Assert: The view should reflect the change immediately
        graph.putEdge(NODE_1, NODE_2);
        assertThat(edgesView).containsExactly(EndpointPair.ordered(NODE_1, NODE_2));

        // Act: Add another edge
        // Assert: The view should reflect the new edge as well
        graph.putEdge(NODE_2, NODE_1);
        assertThat(edgesView).containsExactly(
                EndpointPair.ordered(NODE_1, NODE_2), EndpointPair.ordered(NODE_2, NODE_1));

        // Act: Remove an edge from the graph
        // Assert: The view should be updated
        graph.removeEdge(NODE_1, NODE_2);
        assertThat(edgesView).containsExactly(EndpointPair.ordered(NODE_2, NODE_1));

        // Act: Remove the final edge
        // Assert: The view should now be empty
        graph.removeEdge(NODE_2, NODE_1);
        assertThat(edgesView).isEmpty();

        // Assert: The view itself is unmodifiable
        assertThrows(
                UnsupportedOperationException.class,
                () -> edgesView.add(EndpointPair.ordered(NODE_1, NODE_2)));
    }
}