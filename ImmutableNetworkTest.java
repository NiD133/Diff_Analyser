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

/**
 * Tests for {@link ImmutableNetwork}. Test methods are named using the convention
 * `method_condition_expectedBehavior`.
 */
@RunWith(JUnit4.class)
@NullUnmarked
public class ImmutableNetworkTest {

  private static final String NODE_A = "A";
  private static final String NODE_B = "B";
  private static final String EDGE_AA = "AA";
  private static final String EDGE_AB = "AB";
  private static final Integer EDGE_10 = 10;

  @Test
  public void copyOf_createsImmutableCopy_unaffectedBySourceChanges() {
    // Arrange: Create a mutable network
    MutableNetwork<String, Integer> mutableNetwork = NetworkBuilder.directed().build();
    mutableNetwork.addNode(NODE_A);

    // Act: Create an immutable copy
    ImmutableNetwork<String, Integer> immutableNetwork = ImmutableNetwork.copyOf(mutableNetwork);

    // Assert: The copy is an immutable network instance with the same contents
    assertThat(immutableNetwork.asGraph()).isInstanceOf(ImmutableGraph.class);
    assertThat(immutableNetwork).isNotInstanceOf(MutableNetwork.class);
    assertThat(immutableNetwork).isEqualTo(mutableNetwork);

    // Act: Modify the original mutable network
    mutableNetwork.addNode(NODE_B);

    // Assert: The immutable copy is not affected by the change
    assertThat(immutableNetwork).isNotEqualTo(mutableNetwork);
    assertThat(immutableNetwork.nodes()).containsExactly(NODE_A);
  }

  @Test
  public void copyOf_fromImmutableNetwork_returnsSameInstance() {
    // Arrange: Create an immutable network
    ImmutableNetwork<String, String> immutableNetwork =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());

    // Act: Call copyOf on the immutable network
    Network<String, String> copy = ImmutableNetwork.copyOf(immutableNetwork);

    // Assert: The copy is the same instance (an optimization)
    assertThat(copy).isSameInstanceAs(immutableNetwork);
  }

  @Test
  public void edgesConnecting_inDirectedNetwork() {
    // Arrange
    MutableNetwork<String, String> mutableNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    mutableNetwork.addEdge(NODE_A, NODE_A, EDGE_AA);
    mutableNetwork.addEdge(NODE_A, NODE_B, EDGE_AB);
    Network<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);

    // Assert
    assertThat(network.edgesConnecting(NODE_A, NODE_A)).containsExactly(EDGE_AA);
    assertThat(network.edgesConnecting(NODE_A, NODE_B)).containsExactly(EDGE_AB);
    assertThat(network.edgesConnecting(NODE_B, NODE_A)).isEmpty();
  }

  @Test
  public void edgesConnecting_inUndirectedNetwork() {
    // Arrange
    MutableNetwork<String, String> mutableNetwork =
        NetworkBuilder.undirected().allowsSelfLoops(true).build();
    mutableNetwork.addEdge(NODE_A, NODE_A, EDGE_AA);
    mutableNetwork.addEdge(NODE_A, NODE_B, EDGE_AB);
    Network<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);

    // Assert
    assertThat(network.edgesConnecting(NODE_A, NODE_A)).containsExactly(EDGE_AA);
    assertThat(network.edgesConnecting(NODE_A, NODE_B)).containsExactly(EDGE_AB);
    assertThat(network.edgesConnecting(NODE_B, NODE_A)).containsExactly(EDGE_AB);
  }

  @Test
  public void builder_fromNetworkBuilder_inheritsConfiguration() {
    // Act
    ImmutableNetwork<String, Integer> emptyNetwork =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural())
            .<String, Integer>immutable()
            .build();

    // Assert
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
  public void builder_fromNetworkBuilder_isUnaffectedBySubsequentSourceBuilderChanges() {
    // Arrange: Create a NetworkBuilder and an ImmutableNetwork.Builder from it
    NetworkBuilder<String, Object> networkBuilder =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural());
    ImmutableNetwork.Builder<String, Integer> immutableNetworkBuilder =
        networkBuilder.<String, Integer>immutable();

    // Act: Modify the original NetworkBuilder
    networkBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.<String>unordered());
    ImmutableNetwork<String, Integer> emptyNetwork = immutableNetworkBuilder.build();

    // Assert: The built network used the original configuration, not the modified one
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  @Test
  public void builder_addNode_succeeds() {
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addNode(NODE_A).build();

    assertThat(network.nodes()).containsExactly(NODE_A);
    assertThat(network.edges()).isEmpty();
  }

  @Test
  public void builder_addEdge_withNodes_succeeds() {
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .<String, Integer>immutable()
            .addEdge(NODE_A, NODE_B, EDGE_10)
            .build();

    assertThat(network.nodes()).containsExactly(NODE_A, NODE_B).inOrder();
    assertThat(network.edges()).containsExactly(EDGE_10);
    assertThat(network.incidentNodes(EDGE_10)).isEqualTo(EndpointPair.ordered(NODE_A, NODE_B));
  }

  @Test
  public void builder_addEdge_withEndpointPair_succeeds() {
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .<String, Integer>immutable()
            .addEdge(EndpointPair.ordered(NODE_A, NODE_B), EDGE_10)
            .build();

    assertThat(network.nodes()).containsExactly(NODE_A, NODE_B).inOrder();
    assertThat(network.edges()).containsExactly(EDGE_10);
    assertThat(network.incidentNodes(EDGE_10)).isEqualTo(EndpointPair.ordered(NODE_A, NODE_B));
  }
}