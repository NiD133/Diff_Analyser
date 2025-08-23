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

/** Unit tests for {@link EndpointPair} and {@link Graph#edges()}. */
@RunWith(JUnit4.class)
@NullUnmarked
public final class EndpointPairTest {

  // Constants for test nodes and edges
  private static final Integer NODE_0 = 0;
  private static final Integer NODE_1 = 1;
  private static final Integer NODE_2 = 2;
  private static final Integer NODE_3 = 3;
  private static final Integer NODE_4 = 4;
  private static final String EDGE_12 = "1-2";
  private static final String EDGE_12_ALT = "1-2a";
  private static final String EDGE_21 = "2-1";
  private static final String EDGE_13 = "1-3";
  private static final String EDGE_44 = "4-4";

  // Tests for EndpointPair class

  @Test
  public void orderedEndpointPair_shouldBehaveAsExpected() {
    EndpointPair<String> orderedPair = EndpointPair.ordered("source", "target");

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
  public void unorderedEndpointPair_shouldBehaveAsExpected() {
    EndpointPair<String> unorderedPair = EndpointPair.unordered("chicken", "egg");

    assertThat(unorderedPair.isOrdered()).isFalse();
    assertThat(unorderedPair).containsExactly("chicken", "egg");
    assertThat(ImmutableSet.of(unorderedPair.nodeU(), unorderedPair.nodeV()))
        .containsExactly("chicken", "egg");
    assertThat(unorderedPair.adjacentNode(unorderedPair.nodeU())).isEqualTo(unorderedPair.nodeV());
    assertThat(unorderedPair.adjacentNode(unorderedPair.nodeV())).isEqualTo(unorderedPair.nodeU());
    assertThat(unorderedPair.toString()).contains("chicken");
    assertThat(unorderedPair.toString()).contains("egg");
  }

  @Test
  public void selfLoop_shouldBehaveAsExpected() {
    EndpointPair<String> selfLoop = EndpointPair.unordered("node", "node");

    assertThat(selfLoop.isOrdered()).isFalse();
    assertThat(selfLoop).containsExactly("node", "node");
    assertThat(selfLoop.nodeU()).isEqualTo("node");
    assertThat(selfLoop.nodeV()).isEqualTo("node");
    assertThat(selfLoop.adjacentNode("node")).isEqualTo("node");
    assertThat(selfLoop.toString()).isEqualTo("[node, node]");
  }

  @Test
  public void adjacentNode_whenNodeNotIncident_shouldThrowException() {
    ImmutableList<MutableNetwork<Integer, String>> testNetworks =
        ImmutableList.of(
            NetworkBuilder.directed().<Integer, String>build(),
            NetworkBuilder.undirected().<Integer, String>build());

    for (MutableNetwork<Integer, String> network : testNetworks) {
      network.addEdge(1, 2, EDGE_12);
      EndpointPair<Integer> endpointPair = network.incidentNodes(EDGE_12);
      assertThrows(IllegalArgumentException.class, () -> endpointPair.adjacentNode(3));
    }
  }

  @Test
  public void equals_shouldBehaveAsExpected() {
    EndpointPair<String> orderedPair = EndpointPair.ordered("a", "b");
    EndpointPair<String> orderedMirror = EndpointPair.ordered("b", "a");
    EndpointPair<String> unorderedPair = EndpointPair.unordered("a", "b");
    EndpointPair<String> unorderedMirror = EndpointPair.unordered("b", "a");

    new EqualsTester()
        .addEqualityGroup(orderedPair)
        .addEqualityGroup(orderedMirror)
        .addEqualityGroup(unorderedPair, unorderedMirror)
        .testEquals();
  }

  // Tests for Graph.edges() and Network.asGraph().edges() methods

  @Test
  public void directedGraph_edges_shouldContainExpectedEndpointPairs() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.addNode(NODE_0);
    directedGraph.putEdge(NODE_1, NODE_2);
    directedGraph.putEdge(NODE_2, NODE_1);
    directedGraph.putEdge(NODE_1, NODE_3);
    directedGraph.putEdge(NODE_4, NODE_4);

    assertContainsExactly(
        directedGraph.edges(),
        EndpointPair.ordered(NODE_1, NODE_2),
        EndpointPair.ordered(NODE_2, NODE_1),
        EndpointPair.ordered(NODE_1, NODE_3),
        EndpointPair.ordered(NODE_4, NODE_4));
  }

  @Test
  public void undirectedGraph_edges_shouldContainExpectedEndpointPairs() {
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.addNode(NODE_0);
    undirectedGraph.putEdge(NODE_1, NODE_2);
    undirectedGraph.putEdge(NODE_2, NODE_1); // does nothing
    undirectedGraph.putEdge(NODE_1, NODE_3);
    undirectedGraph.putEdge(NODE_4, NODE_4);

    assertContainsExactly(
        undirectedGraph.edges(),
        EndpointPair.unordered(NODE_1, NODE_2),
        EndpointPair.unordered(NODE_1, NODE_3),
        EndpointPair.unordered(NODE_4, NODE_4));
  }

  @Test
  public void directedNetwork_edges_shouldContainExpectedEndpointPairs() {
    MutableNetwork<Integer, String> directedNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    directedNetwork.addNode(NODE_0);
    directedNetwork.addEdge(NODE_1, NODE_2, EDGE_12);
    directedNetwork.addEdge(NODE_2, NODE_1, EDGE_21);
    directedNetwork.addEdge(NODE_1, NODE_3, EDGE_13);
    directedNetwork.addEdge(NODE_4, NODE_4, EDGE_44);

    assertContainsExactly(
        directedNetwork.asGraph().edges(),
        EndpointPair.ordered(NODE_1, NODE_2),
        EndpointPair.ordered(NODE_2, NODE_1),
        EndpointPair.ordered(NODE_1, NODE_3),
        EndpointPair.ordered(NODE_4, NODE_4));
  }

  @Test
  public void undirectedNetwork_edges_shouldContainExpectedEndpointPairs() {
    MutableNetwork<Integer, String> undirectedNetwork =
        NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();
    undirectedNetwork.addNode(NODE_0);
    undirectedNetwork.addEdge(NODE_1, NODE_2, EDGE_12);
    undirectedNetwork.addEdge(NODE_2, NODE_1, EDGE_12_ALT); // adds parallel edge, won't be in Graph edges
    undirectedNetwork.addEdge(NODE_1, NODE_3, EDGE_13);
    undirectedNetwork.addEdge(NODE_4, NODE_4, EDGE_44);

    assertContainsExactly(
        undirectedNetwork.asGraph().edges(),
        EndpointPair.unordered(NODE_1, NODE_2),
        EndpointPair.unordered(NODE_1, NODE_3),
        EndpointPair.unordered(NODE_4, NODE_4));
  }

  @Test
  public void unmodifiableView_shouldReflectGraphChanges() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().build();
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    directedGraph.putEdge(NODE_1, NODE_2);
    assertContainsExactly(edges, EndpointPair.ordered(NODE_1, NODE_2));

    directedGraph.putEdge(NODE_2, NODE_1);
    assertContainsExactly(edges, EndpointPair.ordered(NODE_1, NODE_2), EndpointPair.ordered(NODE_2, NODE_1));

    directedGraph.removeEdge(NODE_1, NODE_2);
    directedGraph.removeEdge(NODE_2, NODE_1);
    assertContainsExactly(edges);

    assertThrows(
        UnsupportedOperationException.class, () -> edges.add(EndpointPair.ordered(NODE_1, NODE_2)));
  }

  @Test
  public void undirectedGraph_contains_shouldBehaveAsExpected() {
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.putEdge(NODE_1, NODE_1);
    undirectedGraph.putEdge(NODE_1, NODE_2);
    Set<EndpointPair<Integer>> edges = undirectedGraph.edges();

    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.unordered(NODE_1, NODE_1));
    assertThat(edges).contains(EndpointPair.unordered(NODE_1, NODE_2));
    assertThat(edges).contains(EndpointPair.unordered(NODE_2, NODE_1)); // equal to unordered(NODE_1, NODE_2)

    // ordered endpoints not compatible with undirected graph
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_1, NODE_2));

    assertThat(edges).doesNotContain(EndpointPair.unordered(NODE_2, NODE_2)); // edge not present
    assertThat(edges).doesNotContain(EndpointPair.unordered(NODE_3, NODE_4)); // nodes not in graph
  }

  @Test
  public void directedGraph_contains_shouldBehaveAsExpected() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.putEdge(NODE_1, NODE_1);
    directedGraph.putEdge(NODE_1, NODE_2);
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.ordered(NODE_1, NODE_1));
    assertThat(edges).contains(EndpointPair.ordered(NODE_1, NODE_2));

    // unordered endpoints not OK for directed graph (undefined behavior)
    assertThat(edges).doesNotContain(EndpointPair.unordered(NODE_1, NODE_2));

    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_2, NODE_1)); // wrong order
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_2, NODE_2)); // edge not present
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_3, NODE_4)); // nodes not in graph
  }

  // Helper method to assert that a collection contains exactly the specified elements
  private static void assertContainsExactly(Collection<?> collection, Object... elements) {
    assertThat(collection).hasSize(elements.length);
    for (Object element : elements) {
      assertThat(collection).contains(element);
    }
    assertThat(collection).containsExactly(elements);
  }
}