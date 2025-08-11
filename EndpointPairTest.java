package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import java.util.Collection;
import java.util.Set;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Focused tests for EndpointPair and the Set<EndpointPair<N>> views returned by Graph.edges()
 * and Network.asGraph().edges().
 *
 * Test layout:
 * - EndpointPair construction/behavior (ordered, unordered, self-loop, equals, adjacentNode).
 * - Graph.edges() content for directed/undirected graphs.
 * - Network.asGraph().edges() content for directed/undirected networks.
 * - Edges view is live and unmodifiable.
 * - contains() semantics for edges in directed/undirected graphs.
 */
@RunWith(JUnit4.class)
@NullUnmarked
public final class EndpointPairTest {

  // Common node and edge IDs used across tests for readability.
  private static final Integer N0 = 0;
  private static final Integer N1 = 1;
  private static final Integer N2 = 2;
  private static final Integer N3 = 3;
  private static final Integer N4 = 4;

  private static final String E12 = "1-2";
  private static final String E12_A = "1-2a"; // parallel to E12
  private static final String E21 = "2-1";
  private static final String E13 = "1-3";
  private static final String E44 = "4-4";

  // -------------------------------------------------------------------
  // EndpointPair construction and basic behavior
  // -------------------------------------------------------------------

  @Test
  public void orderedPair_basicBehavior() {
    // Given
    EndpointPair<String> orderedPair = EndpointPair.ordered("source", "target");

    // Then
    assertThat(orderedPair.isOrdered()).isTrue();
    assertThat(orderedPair).containsExactly("source", "target").inOrder();
    assertThat(orderedPair.source()).isEqualTo("source");
    assertThat(orderedPair.target()).isEqualTo("target");
    assertThat(orderedPair.nodeU()).isEqualTo("source");
    assertThat(orderedPair.nodeV()).isEqualTo("target");
    assertThat(orderedPair.adjacentNode("source")).isEqualTo("target");
    assertThat(orderedPair.adjacentNode("target")).isEqualTo("source");
    assertThat(orderedPair.toString()).isEqualTo("<source -> target>");
  }

  @Test
  public void unorderedPair_basicBehavior() {
    // Given
    EndpointPair<String> unorderedPair = EndpointPair.unordered("chicken", "egg");

    // Then
    assertThat(unorderedPair.isOrdered()).isFalse();
    assertThat(unorderedPair).containsExactly("chicken", "egg");
    assertThat(ImmutableSet.of(unorderedPair.nodeU(), unorderedPair.nodeV()))
        .containsExactly("chicken", "egg");
    assertThat(unorderedPair.adjacentNode(unorderedPair.nodeU())).isEqualTo(unorderedPair.nodeV());
    assertThat(unorderedPair.adjacentNode(unorderedPair.nodeV())).isEqualTo(unorderedPair.nodeU());
    // String form should include both endpoints (order unspecified).
    assertThat(unorderedPair.toString()).contains("chicken");
    assertThat(unorderedPair.toString()).contains("egg");
  }

  @Test
  public void unorderedPair_selfLoop() {
    // Given
    EndpointPair<String> selfLoop = EndpointPair.unordered("node", "node");

    // Then
    assertThat(selfLoop.isOrdered()).isFalse();
    assertThat(selfLoop).containsExactly("node", "node");
    assertThat(selfLoop.nodeU()).isEqualTo("node");
    assertThat(selfLoop.nodeV()).isEqualTo("node");
    assertThat(selfLoop.adjacentNode("node")).isEqualTo("node");
    assertThat(selfLoop.toString()).isEqualTo("[node, node]");
  }

  @Test
  public void adjacentNode_throwsWhenNodeNotIncident_inDirectedNetwork() {
    // Given
    MutableNetwork<Integer, String> network = NetworkBuilder.directed().build();
    network.addEdge(1, 2, E12);
    EndpointPair<Integer> endpoints = network.incidentNodes(E12);

    // Then
    assertThrows(IllegalArgumentException.class, () -> endpoints.adjacentNode(3));
  }

  @Test
  public void adjacentNode_throwsWhenNodeNotIncident_inUndirectedNetwork() {
    // Given
    MutableNetwork<Integer, String> network = NetworkBuilder.undirected().build();
    network.addEdge(1, 2, E12);
    EndpointPair<Integer> endpoints = network.incidentNodes(E12);

    // Then
    assertThrows(IllegalArgumentException.class, () -> endpoints.adjacentNode(3));
  }

  @Test
  public void equals_orderedAndUnordered() {
    EndpointPair<String> ordered = EndpointPair.ordered("a", "b");
    EndpointPair<String> orderedMirror = EndpointPair.ordered("b", "a");
    EndpointPair<String> unordered = EndpointPair.unordered("a", "b");
    EndpointPair<String> unorderedMirror = EndpointPair.unordered("b", "a");

    new EqualsTester()
        .addEqualityGroup(ordered)
        .addEqualityGroup(orderedMirror)
        .addEqualityGroup(unordered, unorderedMirror)
        .testEquals();
  }

  // -------------------------------------------------------------------
  // Graph.edges() content
  // -------------------------------------------------------------------

  @Test
  public void graph_edges_directed() {
    // Given
    MutableGraph<Integer> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
    graph.addNode(N0); // isolated node
    graph.putEdge(N1, N2);
    graph.putEdge(N2, N1);
    graph.putEdge(N1, N3);
    graph.putEdge(N4, N4); // self-loop

    // Then
    assertContainsExactly(
        graph.edges(),
        EndpointPair.ordered(N1, N2),
        EndpointPair.ordered(N2, N1),
        EndpointPair.ordered(N1, N3),
        EndpointPair.ordered(N4, N4));
  }

  @Test
  public void graph_edges_undirected() {
    // Given
    MutableGraph<Integer> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    graph.addNode(N0); // isolated node
    graph.putEdge(N1, N2);
    graph.putEdge(N2, N1); // no-op for undirected graphs
    graph.putEdge(N1, N3);
    graph.putEdge(N4, N4); // self-loop

    // Then
    assertContainsExactly(
        graph.edges(),
        EndpointPair.unordered(N1, N2),
        EndpointPair.unordered(N1, N3),
        EndpointPair.unordered(N4, N4));
  }

  // -------------------------------------------------------------------
  // Network.asGraph().edges() content
  // -------------------------------------------------------------------

  @Test
  public void network_asGraph_edges_directed() {
    // Given
    MutableNetwork<Integer, String> network =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    network.addNode(N0); // isolated node
    network.addEdge(N1, N2, E12);
    network.addEdge(N2, N1, E21);
    network.addEdge(N1, N3, E13);
    network.addEdge(N4, N4, E44); // self-loop

    // Then
    assertContainsExactly(
        network.asGraph().edges(),
        EndpointPair.ordered(N1, N2),
        EndpointPair.ordered(N2, N1),
        EndpointPair.ordered(N1, N3),
        EndpointPair.ordered(N4, N4));
  }

  @Test
  public void network_asGraph_edges_undirected_parallelEdgesDoNotDuplicateGraphEdges() {
    // Given
    MutableNetwork<Integer, String> network =
        NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();
    network.addNode(N0); // isolated node
    network.addEdge(N1, N2, E12);
    network.addEdge(N2, N1, E12_A); // parallel edge; Graph edges view remains deduplicated
    network.addEdge(N1, N3, E13);
    network.addEdge(N4, N4, E44); // self-loop

    // Then
    assertContainsExactly(
        network.asGraph().edges(),
        EndpointPair.unordered(N1, N2),
        EndpointPair.unordered(N1, N3),
        EndpointPair.unordered(N4, N4));
  }

  // -------------------------------------------------------------------
  // Edges view characteristics
  // -------------------------------------------------------------------

  @Test
  public void edgesLiveView_isUnmodifiable() {
    // Given
    MutableGraph<Integer> graph = GraphBuilder.directed().build();
    Set<EndpointPair<Integer>> edgesView = graph.edges();

    // The view is live.
    graph.putEdge(N1, N2);
    assertContainsExactly(edgesView, EndpointPair.ordered(N1, N2));

    graph.putEdge(N2, N1);
    assertContainsExactly(
        edgesView, EndpointPair.ordered(N1, N2), EndpointPair.ordered(N2, N1));

    graph.removeEdge(N1, N2);
    graph.removeEdge(N2, N1);
    assertContainsExactly(edgesView);

    // And unmodifiable.
    assertThrows(UnsupportedOperationException.class, () -> edgesView.add(EndpointPair.ordered(N1, N2)));
  }

  // -------------------------------------------------------------------
  // contains() semantics on edges views
  // -------------------------------------------------------------------

  @Test
  public void edges_contains_forUndirectedGraph() {
    // Given
    MutableGraph<Integer> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    graph.putEdge(N1, N1);
    graph.putEdge(N1, N2);
    Set<EndpointPair<Integer>> edges = graph.edges();

    // Then
    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.unordered(N1, N1));
    assertThat(edges).contains(EndpointPair.unordered(N1, N2));
    assertThat(edges).contains(EndpointPair.unordered(N2, N1)); // equal to unordered(N1, N2)

    // Ordered endpoint pairs are incompatible with undirected graphs.
    assertThat(edges).doesNotContain(EndpointPair.ordered(N1, N2));

    // Non-existent edges
    assertThat(edges).doesNotContain(EndpointPair.unordered(N2, N2));
    assertThat(edges).doesNotContain(EndpointPair.unordered(N3, N4));
  }

  @Test
  public void edges_contains_forDirectedGraph() {
    // Given
    MutableGraph<Integer> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
    graph.putEdge(N1, N1);
    graph.putEdge(N1, N2);
    Set<EndpointPair<Integer>> edges = graph.edges();

    // Then
    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.ordered(N1, N1));
    assertThat(edges).contains(EndpointPair.ordered(N1, N2));

    // Unordered endpoint pairs are not valid for directed graphs.
    assertThat(edges).doesNotContain(EndpointPair.unordered(N1, N2));

    // Wrong order or non-existent edges
    assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N1));
    assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N2));
    assertThat(edges).doesNotContain(EndpointPair.ordered(N3, N4));
  }

  // -------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------

  @SafeVarargs
  private static <T> void assertContainsExactly(Collection<T> actual, T... expected) {
    assertThat(actual).containsExactly((Object[]) expected);
  }
}