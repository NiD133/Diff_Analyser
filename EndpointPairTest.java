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
  
  // Test data constants for better readability
  private static final Integer NODE_0 = 0;
  private static final Integer NODE_1 = 1;
  private static final Integer NODE_2 = 2;
  private static final Integer NODE_3 = 3;
  private static final Integer NODE_4 = 4;
  
  private static final String EDGE_1_TO_2 = "1-2";
  private static final String EDGE_1_TO_2_ALTERNATE = "1-2a";
  private static final String EDGE_2_TO_1 = "2-1";
  private static final String EDGE_1_TO_3 = "1-3";
  private static final String EDGE_4_SELF_LOOP = "4-4";

  // ===== Tests for EndpointPair class =====

  @Test
  public void orderedEndpointPair_shouldHaveCorrectPropertiesAndBehavior() {
    // Given: An ordered endpoint pair representing a directed edge
    EndpointPair<String> orderedPair = EndpointPair.ordered("source", "target");
    
    // Then: It should be marked as ordered
    assertThat(orderedPair.isOrdered()).isTrue();
    
    // And: It should contain both nodes in the correct order
    assertThat(orderedPair).containsExactly("source", "target").inOrder();
    
    // And: Source and target should be accessible
    assertThat(orderedPair.source()).isEqualTo("source");
    assertThat(orderedPair.target()).isEqualTo("target");
    
    // And: NodeU should be source, nodeV should be target
    assertThat(orderedPair.nodeU()).isEqualTo("source");
    assertThat(orderedPair.nodeV()).isEqualTo("target");
    
    // And: Adjacent node lookup should work correctly
    assertThat(orderedPair.adjacentNode("source")).isEqualTo("target");
    assertThat(orderedPair.adjacentNode("target")).isEqualTo("source");
    
    // And: String representation should show direction
    assertThat(orderedPair.toString()).isEqualTo("<source -> target>");
  }

  @Test
  public void unorderedEndpointPair_shouldHaveCorrectPropertiesAndBehavior() {
    // Given: An unordered endpoint pair representing an undirected edge
    EndpointPair<String> unorderedPair = EndpointPair.unordered("chicken", "egg");
    
    // Then: It should be marked as unordered
    assertThat(unorderedPair.isOrdered()).isFalse();
    
    // And: It should contain both nodes (order doesn't matter)
    assertThat(unorderedPair).containsExactly("chicken", "egg");
    
    // And: NodeU and nodeV should be the two endpoints (in some order)
    assertThat(ImmutableSet.of(unorderedPair.nodeU(), unorderedPair.nodeV()))
        .containsExactly("chicken", "egg");
    
    // And: Adjacent node lookup should work correctly
    assertThat(unorderedPair.adjacentNode(unorderedPair.nodeU())).isEqualTo(unorderedPair.nodeV());
    assertThat(unorderedPair.adjacentNode(unorderedPair.nodeV())).isEqualTo(unorderedPair.nodeU());
    
    // And: String representation should contain both nodes
    assertThat(unorderedPair.toString()).contains("chicken");
    assertThat(unorderedPair.toString()).contains("egg");
  }

  @Test
  public void selfLoopEndpointPair_shouldHandleSameNodeCorrectly() {
    // Given: An endpoint pair representing a self-loop
    EndpointPair<String> selfLoop = EndpointPair.unordered("node", "node");
    
    // Then: It should be unordered
    assertThat(selfLoop.isOrdered()).isFalse();
    
    // And: It should contain the same node twice
    assertThat(selfLoop).containsExactly("node", "node");
    assertThat(selfLoop.nodeU()).isEqualTo("node");
    assertThat(selfLoop.nodeV()).isEqualTo("node");
    
    // And: Adjacent node should be itself
    assertThat(selfLoop.adjacentNode("node")).isEqualTo("node");
    
    // And: String representation should show self-loop format
    assertThat(selfLoop.toString()).isEqualTo("[node, node]");
  }

  @Test
  public void adjacentNode_withNonIncidentNode_shouldThrowException() {
    // Given: Networks with edges
    ImmutableList<MutableNetwork<Integer, String>> testNetworks =
        ImmutableList.of(
            NetworkBuilder.directed().<Integer, String>build(),
            NetworkBuilder.undirected().<Integer, String>build());
    
    for (MutableNetwork<Integer, String> network : testNetworks) {
      // Given: A network with an edge between nodes 1 and 2
      network.addEdge(NODE_1, NODE_2, EDGE_1_TO_2);
      EndpointPair<Integer> endpointPair = network.incidentNodes(EDGE_1_TO_2);
      
      // When/Then: Asking for adjacent node with non-incident node should throw
      assertThrows(
          IllegalArgumentException.class, 
          () -> endpointPair.adjacentNode(NODE_3));
    }
  }

  @Test
  public void endpointPairEquality_shouldFollowOrderedAndUnorderedRules() {
    // Given: Various endpoint pairs
    EndpointPair<String> orderedAB = EndpointPair.ordered("a", "b");
    EndpointPair<String> orderedBA = EndpointPair.ordered("b", "a");
    EndpointPair<String> unorderedAB = EndpointPair.unordered("a", "b");
    EndpointPair<String> unorderedBA = EndpointPair.unordered("b", "a");

    // Then: Equality should follow the correct rules
    new EqualsTester()
        .addEqualityGroup(orderedAB)                    // Ordered pairs are directional
        .addEqualityGroup(orderedBA)                    // Different from orderedAB
        .addEqualityGroup(unorderedAB, unorderedBA)     // Unordered pairs ignore direction
        .testEquals();
  }

  // ===== Tests for Graph.edges() and Network.asGraph().edges() methods =====

  @Test
  public void directedGraphEdges_shouldReturnOrderedEndpointPairs() {
    // Given: A directed graph with various edges including self-loop
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.addNode(NODE_0);                    // Isolated node
    directedGraph.putEdge(NODE_1, NODE_2);           // Regular edge
    directedGraph.putEdge(NODE_2, NODE_1);           // Reverse edge (creates separate edge)
    directedGraph.putEdge(NODE_1, NODE_3);           // Another edge
    directedGraph.putEdge(NODE_4, NODE_4);           // Self-loop
    
    // Then: All edges should be represented as ordered endpoint pairs
    verifyCollectionContainsExactly(
        directedGraph.edges(),
        EndpointPair.ordered(NODE_1, NODE_2),
        EndpointPair.ordered(NODE_2, NODE_1),
        EndpointPair.ordered(NODE_1, NODE_3),
        EndpointPair.ordered(NODE_4, NODE_4));
  }

  @Test
  public void undirectedGraphEdges_shouldReturnUnorderedEndpointPairs() {
    // Given: An undirected graph with various edges including self-loop
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.addNode(NODE_0);                 // Isolated node
    undirectedGraph.putEdge(NODE_1, NODE_2);        // Regular edge
    undirectedGraph.putEdge(NODE_2, NODE_1);        // Same as above (no effect in undirected)
    undirectedGraph.putEdge(NODE_1, NODE_3);        // Another edge
    undirectedGraph.putEdge(NODE_4, NODE_4);        // Self-loop
    
    // Then: All edges should be represented as unordered endpoint pairs
    verifyCollectionContainsExactly(
        undirectedGraph.edges(),
        EndpointPair.unordered(NODE_1, NODE_2),
        EndpointPair.unordered(NODE_1, NODE_3),
        EndpointPair.unordered(NODE_4, NODE_4));
  }

  @Test
  public void directedNetworkAsGraphEdges_shouldReturnOrderedEndpointPairs() {
    // Given: A directed network with various edges including self-loop
    MutableNetwork<Integer, String> directedNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    directedNetwork.addNode(NODE_0);
    directedNetwork.addEdge(NODE_1, NODE_2, EDGE_1_TO_2);
    directedNetwork.addEdge(NODE_2, NODE_1, EDGE_2_TO_1);
    directedNetwork.addEdge(NODE_1, NODE_3, EDGE_1_TO_3);
    directedNetwork.addEdge(NODE_4, NODE_4, EDGE_4_SELF_LOOP);
    
    // Then: Graph view should show ordered endpoint pairs
    verifyCollectionContainsExactly(
        directedNetwork.asGraph().edges(),
        EndpointPair.ordered(NODE_1, NODE_2),
        EndpointPair.ordered(NODE_2, NODE_1),
        EndpointPair.ordered(NODE_1, NODE_3),
        EndpointPair.ordered(NODE_4, NODE_4));
  }

  @Test
  public void undirectedNetworkAsGraphEdges_shouldCollapseParallelEdges() {
    // Given: An undirected network with parallel edges
    MutableNetwork<Integer, String> undirectedNetwork =
        NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();
    undirectedNetwork.addNode(NODE_0);
    undirectedNetwork.addEdge(NODE_1, NODE_2, EDGE_1_TO_2);
    undirectedNetwork.addEdge(NODE_2, NODE_1, EDGE_1_TO_2_ALTERNATE); // Parallel edge
    undirectedNetwork.addEdge(NODE_1, NODE_3, EDGE_1_TO_3);
    undirectedNetwork.addEdge(NODE_4, NODE_4, EDGE_4_SELF_LOOP);
    
    // Then: Graph view should collapse parallel edges into single endpoint pairs
    verifyCollectionContainsExactly(
        undirectedNetwork.asGraph().edges(),
        EndpointPair.unordered(NODE_1, NODE_2),  // Only one pair despite parallel edges
        EndpointPair.unordered(NODE_1, NODE_3),
        EndpointPair.unordered(NODE_4, NODE_4));
  }

  @Test
  public void graphEdges_shouldProvideUnmodifiableLiveView() {
    // Given: A directed graph and its edges view
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().build();
    Set<EndpointPair<Integer>> edgesView = directedGraph.edges();

    // When: Adding an edge to the graph
    directedGraph.putEdge(NODE_1, NODE_2);
    // Then: The view should reflect the change
    verifyCollectionContainsExactly(edgesView, EndpointPair.ordered(NODE_1, NODE_2));

    // When: Adding another edge
    directedGraph.putEdge(NODE_2, NODE_1);
    // Then: The view should reflect both edges
    verifyCollectionContainsExactly(
        edgesView, 
        EndpointPair.ordered(NODE_1, NODE_2), 
        EndpointPair.ordered(NODE_2, NODE_1));

    // When: Removing edges
    directedGraph.removeEdge(NODE_1, NODE_2);
    directedGraph.removeEdge(NODE_2, NODE_1);
    // Then: The view should be empty
    verifyCollectionContainsExactly(edgesView);

    // When/Then: Attempting to modify the view should throw exception
    assertThrows(
        UnsupportedOperationException.class, 
        () -> edgesView.add(EndpointPair.ordered(NODE_1, NODE_2)));
  }

  @Test
  public void undirectedGraphEdges_containsMethod_shouldWorkWithBothOrderings() {
    // Given: An undirected graph with edges
    MutableGraph<Integer> undirectedGraph = GraphBuilder.undirected().allowsSelfLoops(true).build();
    undirectedGraph.putEdge(NODE_1, NODE_1);  // Self-loop
    undirectedGraph.putEdge(NODE_1, NODE_2);  // Regular edge
    Set<EndpointPair<Integer>> edges = undirectedGraph.edges();

    // Then: Should contain exactly 2 edges
    assertThat(edges).hasSize(2);
    
    // And: Should contain the actual endpoint pairs
    assertThat(edges).contains(EndpointPair.unordered(NODE_1, NODE_1));
    assertThat(edges).contains(EndpointPair.unordered(NODE_1, NODE_2));
    
    // And: Should work with reversed node order (since unordered)
    assertThat(edges).contains(EndpointPair.unordered(NODE_2, NODE_1));

    // But: Should not contain ordered endpoint pairs (wrong type for undirected)
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_1, NODE_2));

    // And: Should not contain non-existent edges
    assertThat(edges).doesNotContain(EndpointPair.unordered(NODE_2, NODE_2));
    assertThat(edges).doesNotContain(EndpointPair.unordered(NODE_3, NODE_4));
  }

  @Test
  public void directedGraphEdges_containsMethod_shouldRespectDirection() {
    // Given: A directed graph with edges
    MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    directedGraph.putEdge(NODE_1, NODE_1);  // Self-loop
    directedGraph.putEdge(NODE_1, NODE_2);  // Directed edge
    Set<EndpointPair<Integer>> edges = directedGraph.edges();

    // Then: Should contain exactly 2 edges
    assertThat(edges).hasSize(2);
    
    // And: Should contain the actual ordered endpoint pairs
    assertThat(edges).contains(EndpointPair.ordered(NODE_1, NODE_1));
    assertThat(edges).contains(EndpointPair.ordered(NODE_1, NODE_2));

    // But: Should not contain unordered endpoint pairs (wrong type for directed)
    assertThat(edges).doesNotContain(EndpointPair.unordered(NODE_1, NODE_2));

    // And: Should not contain reversed direction (different edge)
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_2, NODE_1));
    
    // And: Should not contain non-existent edges
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_2, NODE_2));
    assertThat(edges).doesNotContain(EndpointPair.ordered(NODE_3, NODE_4));
  }

  /**
   * Helper method to verify that a collection contains exactly the specified elements.
   * This provides better error messages than just using containsExactly().
   */
  private static void verifyCollectionContainsExactly(Collection<?> actualCollection, Object... expectedElements) {
    // First check size for clearer error message
    assertThat(actualCollection).hasSize(expectedElements.length);
    
    // Then check each element is present
    for (Object expectedElement : expectedElements) {
      assertThat(actualCollection).contains(expectedElement);
    }
    
    // Finally verify exact match
    assertThat(actualCollection).containsExactly(expectedElements);
  }
}