/*
 * Copyright (C) 2014 The Guava Authors
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

import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ImmutableNetwork}. */
@RunWith(JUnit4.class)
@NullUnmarked
public class ImmutableNetworkTest {

  @Test
  public void testCopyOfMutableNetwork_createsImmutableSnapshot() {
    // Initialize a mutable network and add a node
    MutableNetwork<String, Integer> mutableNetwork = NetworkBuilder.directed().build();
    mutableNetwork.addNode("A");
    
    // Create immutable snapshot
    ImmutableNetwork<String, Integer> immutableNetwork = ImmutableNetwork.copyOf(mutableNetwork);

    // Verify immutability characteristics
    assertThat(immutableNetwork.asGraph()).isInstanceOf(ImmutableGraph.class);
    assertThat(immutableNetwork).isNotInstanceOf(MutableNetwork.class);
    
    // Validate initial state equivalence
    assertThat(immutableNetwork).isEqualTo(mutableNetwork);

    // Modify original network and verify snapshot remains unchanged
    mutableNetwork.addNode("B");
    assertThat(immutableNetwork).isNotEqualTo(mutableNetwork);
  }

  @Test
  public void testCopyOfImmutableNetwork_returnsSameInstance() {
    // Create initial immutable network
    Network<String, String> network1 =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());
    
    // Copy existing immutable network
    Network<String, String> network2 = ImmutableNetwork.copyOf(network1);

    // Verify optimization: same instance should be reused
    assertThat(network2).isSameInstanceAs(network1);
  }

  @Test
  public void testEdgesConnecting_directedNetwork() {
    // Build directed network with self-loop and edge
    MutableNetwork<String, String> mutableNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    mutableNetwork.addEdge("A", "A", "AA");  // Self-loop
    mutableNetwork.addEdge("A", "B", "AB");   // Directed edge
    
    ImmutableNetwork<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);

    // Validate connections in directed context
    assertThat(network.edgesConnecting("A", "A")).containsExactly("AA");  // Self-loop
    assertThat(network.edgesConnecting("A", "B")).containsExactly("AB");  // Forward edge
    assertThat(network.edgesConnecting("B", "A")).isEmpty();              // Reverse not present
  }

  @Test
  public void testEdgesConnecting_undirectedNetwork() {
    // Build undirected network with self-loop and edge
    MutableNetwork<String, String> mutableNetwork =
        NetworkBuilder.undirected().allowsSelfLoops(true).build();
    mutableNetwork.addEdge("A", "A", "AA");  // Self-loop
    mutableNetwork.addEdge("A", "B", "AB");   // Undirected edge
    
    ImmutableNetwork<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);

    // Validate symmetric connections in undirected context
    assertThat(network.edgesConnecting("A", "A")).containsExactly("AA");  // Self-loop
    assertThat(network.edgesConnecting("A", "B")).containsExactly("AB");  // Edge A->B
    assertThat(network.edgesConnecting("B", "A")).containsExactly("AB");  // Edge B->A (same edge)
  }

  @Test
  public void testBuilder_appliesNetworkBuilderConfiguration() {
    // Build immutable network with specific configuration
    ImmutableNetwork<String, Integer> emptyNetwork =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural())
            .<String, Integer>immutable()
            .build();

    // Verify configuration was applied
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  /**
   * Tests that the ImmutableNetwork.Builder doesn't change when the creating NetworkBuilder
   * changes.
   */
  @Test
  @SuppressWarnings("CheckReturnValue")
  public void testBuilder_copiesNetworkBuilderState_atCreationTime() {
    // Create initial network builder configuration
    NetworkBuilder<String, Object> networkBuilder =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .<String>nodeOrder(ElementOrder.<String>natural());
    
    // Capture builder state at this point
    ImmutableNetwork.Builder<String, Integer> immutableNetworkBuilder =
        networkBuilder.<String, Integer>immutable();

    // Modify original network builder (should not affect immutable builder)
    networkBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.<String>unordered());

    // Build network from immutable builder
    ImmutableNetwork<String, Integer> emptyNetwork = immutableNetworkBuilder.build();

    // Verify original configuration is preserved
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  @Test
  public void testBuilder_addNode() {
    // Build network with single node
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addNode("A").build();

    // Validate node exists and no edges present
    assertThat(network.nodes()).containsExactly("A");
    assertThat(network.edges()).isEmpty();
  }

  @Test
  public void testBuilder_addEdgeWithNodes() {
    // Build network with edge using node pair
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addEdge("A", "B", 10).build();

    // Validate nodes and edge
    assertThat(network.nodes()).containsExactly("A", "B");
    assertThat(network.edges()).containsExactly(10);
    assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered("A", "B"));
  }

  @Test
  public void testBuilder_addEdgeWithEndpointPair() {
    // Build network with edge using EndpointPair
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .<String, Integer>immutable()
            .addEdge(EndpointPair.ordered("A", "B"), 10)
            .build();

    // Validate nodes and edge
    assertThat(network.nodes()).containsExactly("A", "B");
    assertThat(network.edges()).containsExactly(10);
    assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered("A", "B"));
  }
}