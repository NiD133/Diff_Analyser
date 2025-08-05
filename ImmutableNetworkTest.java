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

  // Test nodes and edges for better readability
  private static final String NODE_A = "A";
  private static final String NODE_B = "B";
  private static final String EDGE_AA = "AA";
  private static final String EDGE_AB = "AB";
  private static final Integer EDGE_10 = 10;

  @Test
  public void copyOf_createsCopyThatIsImmutableAndIndependent() {
    // Given: A mutable network with one node
    MutableNetwork<String, Integer> originalMutableNetwork = NetworkBuilder.directed().build();
    originalMutableNetwork.addNode(NODE_A);
    
    // When: Creating an immutable copy
    ImmutableNetwork<String, Integer> immutableCopy = ImmutableNetwork.copyOf(originalMutableNetwork);

    // Then: The copy should be immutable and equal to the original
    assertThat(immutableCopy.asGraph()).isInstanceOf(ImmutableGraph.class);
    assertThat(immutableCopy).isNotInstanceOf(MutableNetwork.class);
    assertThat(immutableCopy).isEqualTo(originalMutableNetwork);

    // And: Changes to the original should not affect the immutable copy
    originalMutableNetwork.addNode(NODE_B);
    assertThat(immutableCopy).isNotEqualTo(originalMutableNetwork);
  }

  @Test
  public void copyOf_whenGivenImmutableNetwork_returnsOriginalInstance() {
    // Given: An existing immutable network
    Network<String, String> originalImmutableNetwork =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());
    
    // When: Creating a copy of the immutable network
    Network<String, String> copyOfImmutableNetwork = ImmutableNetwork.copyOf(originalImmutableNetwork);

    // Then: Should return the same instance (optimization)
    assertThat(copyOfImmutableNetwork).isSameInstanceAs(originalImmutableNetwork);
  }

  @Test
  public void edgesConnecting_inDirectedNetwork_respectsDirection() {
    // Given: A directed network with self-loop and regular edge
    MutableNetwork<String, String> mutableDirectedNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    mutableDirectedNetwork.addEdge(NODE_A, NODE_A, EDGE_AA);
    mutableDirectedNetwork.addEdge(NODE_A, NODE_B, EDGE_AB);
    Network<String, String> directedNetwork = ImmutableNetwork.copyOf(mutableDirectedNetwork);

    // Then: Self-loop should be found
    assertThat(directedNetwork.edgesConnecting(NODE_A, NODE_A)).containsExactly(EDGE_AA);
    
    // And: Edge should be found in correct direction
    assertThat(directedNetwork.edgesConnecting(NODE_A, NODE_B)).containsExactly(EDGE_AB);
    
    // And: No edge should be found in reverse direction
    assertThat(directedNetwork.edgesConnecting(NODE_B, NODE_A)).isEmpty();
  }

  @Test
  public void edgesConnecting_inUndirectedNetwork_ignoresDirection() {
    // Given: An undirected network with self-loop and regular edge
    MutableNetwork<String, String> mutableUndirectedNetwork =
        NetworkBuilder.undirected().allowsSelfLoops(true).build();
    mutableUndirectedNetwork.addEdge(NODE_A, NODE_A, EDGE_AA);
    mutableUndirectedNetwork.addEdge(NODE_A, NODE_B, EDGE_AB);
    Network<String, String> undirectedNetwork = ImmutableNetwork.copyOf(mutableUndirectedNetwork);

    // Then: Self-loop should be found
    assertThat(undirectedNetwork.edgesConnecting(NODE_A, NODE_A)).containsExactly(EDGE_AA);
    
    // And: Edge should be found in both directions
    assertThat(undirectedNetwork.edgesConnecting(NODE_A, NODE_B)).containsExactly(EDGE_AB);
    assertThat(undirectedNetwork.edgesConnecting(NODE_B, NODE_A)).containsExactly(EDGE_AB);
  }

  @Test
  public void builder_appliesNetworkBuilderConfiguration() {
    // When: Building an immutable network with specific configuration
    ImmutableNetwork<String, Integer> configuredNetwork =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural())
            .<String, Integer>immutable()
            .build();

    // Then: Configuration should be preserved
    assertThat(configuredNetwork.isDirected()).isTrue();
    assertThat(configuredNetwork.allowsSelfLoops()).isTrue();
    assertThat(configuredNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  @Test
  @SuppressWarnings("CheckReturnValue")
  public void builder_isIsolatedFromOriginalNetworkBuilderChanges() {
    // Given: A network builder with initial configuration
    NetworkBuilder<String, Object> originalNetworkBuilder =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .<String>nodeOrder(ElementOrder.<String>natural());
    
    // When: Creating an immutable builder from the network builder
    ImmutableNetwork.Builder<String, Integer> immutableBuilder =
        originalNetworkBuilder.<String, Integer>immutable();

    // And: Modifying the original network builder
    originalNetworkBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.<String>unordered());

    // Then: The immutable builder should retain the original configuration
    ImmutableNetwork<String, Integer> builtNetwork = immutableBuilder.build();
    assertThat(builtNetwork.isDirected()).isTrue();
    assertThat(builtNetwork.allowsSelfLoops()).isTrue();
    assertThat(builtNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  @Test
  public void builder_addNode_createsNetworkWithSingleNode() {
    // When: Building a network with a single node
    ImmutableNetwork<String, Integer> networkWithSingleNode =
        NetworkBuilder.directed().<String, Integer>immutable()
            .addNode(NODE_A)
            .build();

    // Then: Network should contain only the added node and no edges
    assertThat(networkWithSingleNode.nodes()).containsExactly(NODE_A);
    assertThat(networkWithSingleNode.edges()).isEmpty();
  }

  @Test
  public void builder_addEdgeWithNodes_createsNetworkWithEdgeAndNodes() {
    // When: Building a network by adding an edge between two nodes
    ImmutableNetwork<String, Integer> networkWithEdge =
        NetworkBuilder.directed().<String, Integer>immutable()
            .addEdge(NODE_A, NODE_B, EDGE_10)
            .build();

    // Then: Network should contain both nodes and the connecting edge
    assertThat(networkWithEdge.nodes()).containsExactly(NODE_A, NODE_B);
    assertThat(networkWithEdge.edges()).containsExactly(EDGE_10);
    assertThat(networkWithEdge.incidentNodes(EDGE_10)).isEqualTo(EndpointPair.ordered(NODE_A, NODE_B));
  }

  @Test
  public void builder_addEdgeWithEndpointPair_createsNetworkWithEdgeAndNodes() {
    // When: Building a network by adding an edge using EndpointPair
    ImmutableNetwork<String, Integer> networkWithEdge =
        NetworkBuilder.directed().<String, Integer>immutable()
            .addEdge(EndpointPair.ordered(NODE_A, NODE_B), EDGE_10)
            .build();

    // Then: Network should contain both nodes and the connecting edge
    assertThat(networkWithEdge.nodes()).containsExactly(NODE_A, NODE_B);
    assertThat(networkWithEdge.edges()).containsExactly(EDGE_10);
    assertThat(networkWithEdge.incidentNodes(EDGE_10)).isEqualTo(EndpointPair.ordered(NODE_A, NODE_B));
  }
}