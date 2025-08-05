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
import java.util.Collection;
import java.util.Set;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link EndpointPair} and {@link Graph#edges()}. */
@RunWith(JUnit4.class)
@NullUnmarked
public final class EndpointPairTest {
  // Test node constants
  private static final Integer N0 = 0;
  private static final Integer N1 = 1;
  private static final Integer N2 = 2;
  private static final Integer N3 = 3;
  private static final Integer N4 = 4;
  
  // Test edge constants
  private static final String EDGE_1_2 = "1-2";
  private static final String EDGE_1_2_ALT = "1-2a";
  private static final String EDGE_2_1 = "2-1";
  private static final String EDGE_1_3 = "1-3";
  private static final String EDGE_4_4 = "4-4";

  // ===================================================================
  // Tests for EndpointPair class
  // ===================================================================

  @Test
  public void testOrderedEndpointPair_properties() {
    // Arrange
    EndpointPair<String> ordered = EndpointPair.ordered("source", "target");

    // Assert ordered properties
    assertThat(ordered.isOrdered()).isTrue();
    assertThat(ordered).containsExactly("source", "target").inOrder();
    
    // Assert endpoint accessors
    assertThat(ordered.source()).isEqualTo("source");
    assertThat(ordered.target()).isEqualTo("target");
    assertThat(ordered.nodeU()).isEqualTo("source");
    assertThat(ordered.nodeV()).isEqualTo("target");
    
    // Assert adjacent node behavior
    assertThat(ordered.adjacentNode("source")).isEqualTo("target");
    assertThat(ordered.adjacentNode("target")).isEqualTo("source");
    
    // Assert string representation
    assertThat(ordered.toString()).isEqualTo("<source -> target>");
  }

  @Test
  public void testUnorderedEndpointPair_properties() {
    // Arrange
    EndpointPair<String> unordered = EndpointPair.unordered("chicken", "egg");

    // Assert unordered properties
    assertThat(unordered.isOrdered()).isFalse();
    assertThat(unordered).containsExactly("chicken", "egg");
    
    // Assert endpoint consistency
    assertThat(ImmutableSet.of(unordered.nodeU(), unordered.nodeV()))
        .containsExactly("chicken", "egg");
    
    // Assert symmetric adjacent node behavior
    assertThat(unordered.adjacentNode(unordered.nodeU())).isEqualTo(unordered.nodeV());
    assertThat(unordered.adjacentNode(unordered.nodeV())).isEqualTo(unordered.nodeU());
    
    // Assert string representation contains both nodes
    assertThat(unordered.toString()).contains("chicken");
    assertThat(unordered.toString()).contains("egg");
  }

  @Test
  public void testSelfLoop_unordered() {
    // Arrange
    EndpointPair<String> unordered = EndpointPair.unordered("node", "node");

    // Assert self-loop properties
    assertThat(unordered.isOrdered()).isFalse();
    assertThat(unordered).containsExactly("node", "node");
    
    // Assert endpoints are identical
    assertThat(unordered.nodeU()).isEqualTo("node");
    assertThat(unordered.nodeV()).isEqualTo("node");
    
    // Assert adjacent node behavior
    assertThat(unordered.adjacentNode("node")).isEqualTo("node");
    
    // Assert string representation
    assertThat(unordered.toString()).isEqualTo("[node, node]");
  }

  @Test
  public void testAdjacentNode_nodeNotIncident_throwsException() {
    // Create test networks (directed and undirected)
    ImmutableList<MutableNetwork<Integer, String>> testNetworks = ImmutableList.of(
        NetworkBuilder.directed().<Integer, String>build(),
        NetworkBuilder.undirected().<Integer, String>build()
    );

    for (MutableNetwork<Integer, String> network : testNetworks) {
      // Add edge and get its endpoints
      network.addEdge(N1, N2, EDGE_1_2);
      EndpointPair<Integer> endpointPair = network.incidentNodes(EDGE_1_2);

      // Verify accessing adjacent node for non-incident node throws
      assertThrows(IllegalArgumentException.class, () -> endpointPair.adjacentNode(N3));
    }
  }

  @Test
  public void testEquals_differentTypes() {
    // Create different endpoint pair configurations
    EndpointPair<String> orderedAtoB = EndpointPair.ordered("a", "b");
    EndpointPair<String> orderedBtoA = EndpointPair.ordered("b", "a");
    EndpointPair<String> unorderedAandB = EndpointPair.unordered("a", "b");
    EndpointPair<String> unorderedBandA = EndpointPair.unordered("b", "a");

    // Group by logical equality
    new EqualsTester()
        .addEqualityGroup(orderedAtoB)  // Ordered pairs are direction-sensitive
        .addEqualityGroup(orderedBtoA)  // Different direction is not equal
        .addEqualityGroup(unorderedAandB, unorderedBandA)  // Unordered pairs are direction-agnostic
        .testEquals();
  }

  // ===================================================================
  // Tests for Graph.edges() behavior
  // ===================================================================

  @Test
  public void testGraphEdges_directedGraph() {
    // Arrange: build directed graph
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.addNode(N0);
    directedGraph.putEdge(N1, N2);
    directedGraph.putEdge(N2, N1);  // Reverse direction
    directedGraph.putEdge(N1, N3);
    directedGraph.putEdge(N4, N4);  // Self-loop

    // Assert edge set contains all directed pairs
    containsExactlySanityCheck(
        directedGraph.edges(),
        EndpointPair.ordered(N1, N2),
        EndpointPair.ordered(N2, N1),
        EndpointPair.ordered(N1, N3),
        EndpointPair.ordered(N4, N4)
    );
  }

  @Test
  public void testGraphEdges_undirectedGraph() {
    // Arrange: build undirected graph
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.addNode(N0);
    undirectedGraph.putEdge(N1, N2);
    undirectedGraph.putEdge(N2, N1);  // Duplicate ignored in undirected graph
    undirectedGraph.putEdge(N1, N3);
    undirectedGraph.putEdge(N4, N4);  // Self-loop

    // Assert edge set (unordered pairs)
    containsExactlySanityCheck(
        undirectedGraph.edges(),
        EndpointPair.unordered(N1, N2),
        EndpointPair.unordered(N1, N3),
        EndpointPair.unordered(N4, N4)
    );
  }

  @Test
  public void testGraphEdges_directedNetwork() {
    // Arrange: build directed network
    MutableNetwork<Integer, String> directedNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    directedNetwork.addNode(N0);
    directedNetwork.addEdge(N1, N2, EDGE_1_2);
    directedNetwork.addEdge(N2, N1, EDGE_2_1);  // Reverse edge
    directedNetwork.addEdge(N1, N3, EDGE_1_3);
    directedNetwork.addEdge(N4, N4, EDGE_4_4);  // Self-loop

    // Assert graph edges (converted to EndpointPairs)
    containsExactlySanityCheck(
        directedNetwork.asGraph().edges(),
        EndpointPair.ordered(N1, N2),
        EndpointPair.ordered(N2, N1),
        EndpointPair.ordered(N1, N3),
        EndpointPair.ordered(N4, N4)
    );
  }

  @Test
  public void testGraphEdges_undirectedNetwork() {
    // Arrange: build undirected network
    MutableNetwork<Integer, String> undirectedNetwork =
        NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();
    undirectedNetwork.addNode(N0);
    undirectedNetwork.addEdge(N1, N2, EDGE_1_2);
    // Parallel edge - included in network but not in asGraph().edges()
    undirectedNetwork.addEdge(N2, N1, EDGE_1_2_ALT);
    undirectedNetwork.addEdge(N1, N3, EDGE_1_3);
    undirectedNetwork.addEdge(N4, N4, EDGE_4_4);  // Self-loop

    // Assert graph edges (parallel edges not present in graph view)
    containsExactlySanityCheck(
        undirectedNetwork.asGraph().edges(),
        EndpointPair.unordered(N1, N2),
        EndpointPair.unordered(N1, N3),
        EndpointPair.unordered(N4, N4)
    );
  }

  @Test
  public void testGraphEdges_unmodifiableView() {
    // Arrange
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().build();
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    // Add edge and verify
    directedGraph.putEdge(N1, N2);
    containsExactlySanityCheck(edges, EndpointPair.ordered(N1, N2));

    // Add another edge and verify both
    directedGraph.putEdge(N2, N1);
    containsExactlySanityCheck(
        edges, 
        EndpointPair.ordered(N1, N2),
        EndpointPair.ordered(N2, N1)
    );

    // Remove edges and verify empty
    directedGraph.removeEdge(N1, N2);
    directedGraph.removeEdge(N2, N1);
    containsExactlySanityCheck(edges);

    // Verify view is unmodifiable
    assertThrows(
        UnsupportedOperationException.class, 
        () -> edges.add(EndpointPair.ordered(N1, N2))
    );
  }

  // ===================================================================
  // Tests for contains() behavior in edge sets
  // ===================================================================

  @Test
  public void testGraphEdges_undirected_contains() {
    // Arrange: build undirected graph
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.putEdge(N1, N1);  // Self-loop
    undirectedGraph.putEdge(N1, N2);
    Set<EndpointPair<Integer>> edges = undirectedGraph.edges();

    // Assert size and expected contents
    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.unordered(N1, N1));
    assertThat(edges).contains(EndpointPair.unordered(N1, N2));
    
    // Unordered pairs are direction-agnostic
    assertThat(edges).contains(EndpointPair.unordered(N2, N1)); 

    // Negative cases
    assertThat(edges)
        .doesNotContain(EndpointPair.ordered(N1, N2));  // Wrong pair type
    assertThat(edges)
        .doesNotContain(EndpointPair.unordered(N2, N2)); // Not present
    assertThat(edges)
        .doesNotContain(EndpointPair.unordered(N3, N4)); // Nodes not in graph
  }

  @Test
  public void testGraphEdges_directed_contains() {
    // Arrange: build directed graph
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.putEdge(N1, N1);  // Self-loop
    directedGraph.putEdge(N1, N2);
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    // Assert size and expected contents
    assertThat(edges).hasSize(2);
    assertThat(edges).contains(EndpointPair.ordered(N1, N1));
    assertThat(edges).contains(EndpointPair.ordered(N1, N2));
    
    // Negative cases
    assertThat(edges)
        .doesNotContain(EndpointPair.unordered(N1, N2));  // Wrong pair type
    assertThat(edges)
        .doesNotContain(EndpointPair.ordered(N2, N1));    // Wrong direction
    assertThat(edges)
        .doesNotContain(EndpointPair.ordered(N2, N2));    // Not present
    assertThat(edges)
        .doesNotContain(EndpointPair.ordered(N3, N4));    // Nodes not in graph
  }

  // ===================================================================
  // Helper methods
  // ===================================================================

  /**
   * Verifies a collection contains exactly the given elements in any order.
   * Performs multiple checks to provide better test failure messages.
   * 
   * @param collection the collection to verify
   * @param varargs the expected elements
   */
  private static void containsExactlySanityCheck(Collection<?> collection, Object... varargs) {
    // Verify size matches
    assertThat(collection).hasSize(varargs.length);
    
    // Verify each expected element is present
    for (Object obj : varargs) {
      assertThat(collection).contains(obj);
    }
    
    // Verify no extra elements
    assertThat(collection).containsExactly(varargs);
  }
}