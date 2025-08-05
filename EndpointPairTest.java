/*
 * Copyright (C) 2016 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import java.util.Set;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link EndpointPair}.
 *
 * <p>This test class is divided into two sections:
 * <ol>
 *   <li>Unit tests for the {@link EndpointPair} class itself.
 *   <li>Integration tests for the behavior of {@link Graph#edges()} and
 *       {@link Network#asGraph()#edges()}.
 * </ol>
 */
@RunWith(JUnit4.class)
@NullUnmarked
public final class EndpointPairTest {
  private static final Integer N0 = 0;
  private static final Integer N1 = 1;
  private static final Integer N2 = 2;
  private static final Integer N3 = 3;
  private static final Integer N4 = 4;
  private static final String E12 = "1-2";
  private static final String E12_A = "1-2a";
  private static final String E21 = "2-1";
  private static final String E13 = "1-3";
  private static final String E44 = "4-4";

  // --------------------------------------------------------------------------------
  // Unit tests for the EndpointPair class
  // --------------------------------------------------------------------------------

  @Test
  public void orderedPair_shouldBeOrderedAndReturnCorrectEndpoints() {
    EndpointPair<String> ordered = EndpointPair.ordered("source", "target");

    assertThat(ordered.isOrdered()).isTrue();
    assertThat(ordered.source()).isEqualTo("source");
    assertThat(ordered.target()).isEqualTo("target");
    assertThat(ordered.nodeU()).isEqualTo("source");
    assertThat(ordered.nodeV()).isEqualTo("target");
    assertThat(ordered.adjacentNode("source")).isEqualTo("target");
    assertThat(ordered.adjacentNode("target")).isEqualTo("source");
    assertThat(ordered).containsExactly("source", "target").inOrder();
    assertThat(ordered.toString()).isEqualTo("<source -> target>");
  }

  @Test
  public void unorderedPair_shouldBeUnorderedAndReturnCorrectEndpoints() {
    EndpointPair<String> unordered = EndpointPair.unordered("chicken", "egg");

    assertThat(unordered.isOrdered()).isFalse();
    assertThat(ImmutableSet.of(unordered.nodeU(), unordered.nodeV()))
        .containsExactly("chicken", "egg");
    assertThat(unordered.adjacentNode(unordered.nodeU())).isEqualTo(unordered.nodeV());
    assertThat(unordered.adjacentNode(unordered.nodeV())).isEqualTo(unordered.nodeU());
    assertThat(unordered.toString()).startsWith("[");
    assertThat(unordered.toString()).endsWith("]");
    assertThat(unordered.toString()).contains("chicken");
    assertThat(unordered.toString()).contains("egg");
  }

  @Test
  public void selfLoop_shouldReturnSameNodeForEndpoints() {
    EndpointPair<String> selfLoop = EndpointPair.unordered("node", "node");

    assertThat(selfLoop.isOrdered()).isFalse();
    assertThat(selfLoop.nodeU()).isEqualTo("node");
    assertThat(selfLoop.nodeV()).isEqualTo("node");
    assertThat(selfLoop.adjacentNode("node")).isEqualTo("node");
    assertThat(selfLoop).containsExactly("node", "node");
    assertThat(selfLoop.toString()).isEqualTo("[node, node]");
  }

  @Test
  public void adjacentNode_whenNodeIsNotInPair_shouldThrowException() {
    // Test behavior for pairs derived from both directed and undirected graphs.
    ImmutableList<MutableNetwork<Integer, String>> testNetworks =
        ImmutableList.of(
            NetworkBuilder.directed().<Integer, String>build(),
            NetworkBuilder.undirected().<Integer, String>build());

    for (MutableNetwork<Integer, String> network : testNetworks) {
      network.addEdge(N1, N2, E12);
      EndpointPair<Integer> endpointPair = network.incidentNodes(E12);
      assertThrows(IllegalArgumentException.class, () -> endpointPair.adjacentNode(N3));
    }
  }

  @Test
  public void equals_shouldAdhereToContractForOrderedAndUnorderedPairs() {
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

  // --------------------------------------------------------------------------------
  // Integration tests for Graph.edges() and Network.asGraph().edges()
  // --------------------------------------------------------------------------------

  @Test
  public void edges_onDirectedGraph_shouldReturnOrderedPairs() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.addNode(N0); // node with no incident edges
    directedGraph.putEdge(N1, N2);
    directedGraph.putEdge(N2, N1);
    directedGraph.putEdge(N1, N3);
    directedGraph.putEdge(N4, N4); // self-loop

    // The order of edges is not guaranteed, so we use containsExactlyInAnyOrder.
    assertThat(directedGraph.edges())
        .containsExactlyInAnyOrder(
            EndpointPair.ordered(N1, N2),
            EndpointPair.ordered(N2, N1),
            EndpointPair.ordered(N1, N3),
            EndpointPair.ordered(N4, N4));
  }

  @Test
  public void edges_onUndirectedGraph_shouldReturnUnorderedPairs() {
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.addNode(N0); // node with no incident edges
    undirectedGraph.putEdge(N1, N2);
    undirectedGraph.putEdge(N2, N1); // equivalent to the above, does nothing
    undirectedGraph.putEdge(N1, N3);
    undirectedGraph.putEdge(N4, N4); // self-loop

    assertThat(undirectedGraph.edges())
        .containsExactlyInAnyOrder(
            EndpointPair.unordered(N1, N2),
            EndpointPair.unordered(N1, N3),
            EndpointPair.unordered(N4, N4));
  }

  @Test
  public void asGraph_edges_onDirectedNetwork_shouldReturnOrderedPairs() {
    MutableNetwork<Integer, String> directedNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    directedNetwork.addNode(N0);
    directedNetwork.addEdge(N1, N2, E12);
    directedNetwork.addEdge(N2, N1, E21);
    directedNetwork.addEdge(N1, N3, E13);
    directedNetwork.addEdge(N4, N4, E44);

    assertThat(directedNetwork.asGraph().edges())
        .containsExactlyInAnyOrder(
            EndpointPair.ordered(N1, N2),
            EndpointPair.ordered(N2, N1),
            EndpointPair.ordered(N1, N3),
            EndpointPair.ordered(N4, N4));
  }

  @Test
  public void asGraph_edges_onUndirectedNetwork_shouldReturnUnorderedPairs() {
    MutableNetwork<Integer, String> undirectedNetwork =
        NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();
    undirectedNetwork.addNode(N0);
    undirectedNetwork.addEdge(N1, N2, E12);
    undirectedNetwork.addEdge(N2, N1, E12_A); // parallel edge; won't be in asGraph().edges()
    undirectedNetwork.addEdge(N1, N3, E13);
    undirectedNetwork.addEdge(N4, N4, E44);

    assertThat(undirectedNetwork.asGraph().edges())
        .containsExactlyInAnyOrder(
            EndpointPair.unordered(N1, N2),
            EndpointPair.unordered(N1, N3),
            EndpointPair.unordered(N4, N4));
  }

  @Test
  public void edges_viewShouldReflectGraphModifications() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().build();
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    assertThat(edges).isEmpty();

    directedGraph.putEdge(N1, N2);
    assertThat(edges).containsExactly(EndpointPair.ordered(N1, N2));

    directedGraph.putEdge(N2, N1);
    assertThat(edges)
        .containsExactlyInAnyOrder(EndpointPair.ordered(N1, N2), EndpointPair.ordered(N2, N1));

    directedGraph.removeEdge(N1, N2);
    assertThat(edges).containsExactly(EndpointPair.ordered(N2, N1));

    directedGraph.removeEdge(N2, N1);
    assertThat(edges).isEmpty();
  }

  @Test
  public void edges_viewShouldBeUnmodifiable() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().build();
    Set<EndpointPair<Integer>> edges = directedGraph.edges();
    directedGraph.putEdge(N1, N2); // ensure the set is not empty

    assertThrows(
        UnsupportedOperationException.class, () -> edges.add(EndpointPair.ordered(N2, N3)));
  }

  @Test
  public void edges_onUndirectedGraph_contains_shouldRespectUnorderedEquality() {
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.putEdge(N1, N1);
    undirectedGraph.putEdge(N1, N2);
    Set<EndpointPair<Integer>> edges = undirectedGraph.edges();

    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.unordered(N1, N1));
    assertThat(edges).contains(EndpointPair.unordered(N1, N2));
    assertThat(edges).contains(EndpointPair.unordered(N2, N1)); // equal to unordered(N1, N2)

    // An ordered pair is never equal to an unordered one.
    assertThat(edges).doesNotContain(EndpointPair.ordered(N1, N2));

    assertThat(edges).doesNotContain(EndpointPair.unordered(N2, N2)); // edge not present
    assertThat(edges).doesNotContain(EndpointPair.unordered(N3, N4)); // nodes not in graph
  }

  @Test
  public void edges_onDirectedGraph_contains_shouldRespectOrderedEquality() {
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.putEdge(N1, N1);
    directedGraph.putEdge(N1, N2);
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.ordered(N1, N1));
    assertThat(edges).contains(EndpointPair.ordered(N1, N2));

    // An unordered pair is never equal to an ordered one.
    assertThat(edges).doesNotContain(EndpointPair.unordered(N1, N2));

    assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N1)); // wrong order
    assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N2)); // edge not present
    assertThat(edges).doesNotContain(EndpointPair.ordered(N3, N4)); // nodes not in graph
  }
}