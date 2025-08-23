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
 * Focused tests for ImmutableNetwork:
 * - Copy semantics and immutability
 * - edgesConnecting behavior for directed/undirected
 * - Builder configuration and element addition
 */
@RunWith(JUnit4.class)
@NullUnmarked
public class ImmutableNetworkTest {

  // Common test fixtures
  private static final String A = "A";
  private static final String B = "B";
  private static final String EDGE_AA = "AA";
  private static final String EDGE_AB = "AB";

  @Test
  public void immutableNetwork_equalityAndIndependence() {
    // Arrange: create a mutable network with a single node
    MutableNetwork<String, Integer> mutable = NetworkBuilder.directed().build();
    mutable.addNode(A);

    // Act: copy to immutable
    ImmutableNetwork<String, Integer> immutable = ImmutableNetwork.copyOf(mutable);

    // Assert: type and equality
    assertThat(immutable.asGraph()).isInstanceOf(ImmutableGraph.class);
    assertThat(immutable).isNotInstanceOf(MutableNetwork.class);
    assertThat(immutable).isEqualTo(mutable);

    // When the mutable source changes, the immutable copy remains unchanged
    mutable.addNode(B);
    assertThat(immutable).isNotEqualTo(mutable);
  }

  @Test
  public void copyOf_onImmutableNetwork_returnsSameInstance() {
    // Arrange: create an empty immutable network
    Network<String, String> network1 =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());

    // Act: copying an immutable network should be optimized to return the same instance
    Network<String, String> network2 = ImmutableNetwork.copyOf(network1);

    // Assert
    assertThat(network2).isSameInstanceAs(network1);
  }

  @Test
  public void edgesConnecting_directed() {
    // Arrange: directed network allowing self-loops
    MutableNetwork<String, String> mutable = newDirectedStringNetworkAllowingSelfLoops();
    mutable.addEdge(A, A, EDGE_AA);
    mutable.addEdge(A, B, EDGE_AB);

    Network<String, String> network = ImmutableNetwork.copyOf(mutable);

    // Assert: direction matters
    assertThat(network.edgesConnecting(A, A)).containsExactly(EDGE_AA);
    assertThat(network.edgesConnecting(A, B)).containsExactly(EDGE_AB);
    assertThat(network.edgesConnecting(B, A)).isEmpty();
  }

  @Test
  public void edgesConnecting_undirected() {
    // Arrange: undirected network allowing self-loops
    MutableNetwork<String, String> mutable = newUndirectedStringNetworkAllowingSelfLoops();
    mutable.addEdge(A, A, EDGE_AA);
    mutable.addEdge(A, B, EDGE_AB);

    Network<String, String> network = ImmutableNetwork.copyOf(mutable);

    // Assert: A-B and B-A are equivalent in an undirected network
    assertThat(network.edgesConnecting(A, A)).containsExactly(EDGE_AA);
    assertThat(network.edgesConnecting(A, B)).containsExactly(EDGE_AB);
    assertThat(network.edgesConnecting(B, A)).containsExactly(EDGE_AB);
  }

  @Test
  public void builder_appliesNetworkBuilderConfig() {
    // Arrange + Act: build with a directed builder, self-loops allowed, and natural node order
    ImmutableNetwork<String, Integer> emptyNetwork =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural())
            .<String, Integer>immutable()
            .build();

    // Assert: config copied onto the immutable network
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  /**
   * Verifies that the ImmutableNetwork.Builder captures the configuration at creation time and is
   * not affected by subsequent changes to the originating NetworkBuilder.
   */
  @Test
  @SuppressWarnings("CheckReturnValue")
  public void builder_copiesNetworkBuilderConfiguration() {
    // Arrange: start from a directed builder with self-loops and natural order
    NetworkBuilder<String, Object> baseBuilder =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .<String>nodeOrder(ElementOrder.<String>natural());

    // Create the immutable builder from the base builder
    ImmutableNetwork.Builder<String, Integer> immutableBuilder = baseBuilder.<String, Integer>immutable();

    // Mutate the base builder afterwards; this must not affect the immutable builder
    baseBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.<String>unordered());

    // Act
    ImmutableNetwork<String, Integer> emptyNetwork = immutableBuilder.build();

    // Assert: original configuration is preserved
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  @Test
  public void builder_addNode_addsOnlyNode() {
    // Act
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addNode(A).build();

    // Assert
    assertThat(network.nodes()).containsExactly(A);
    assertThat(network.edges()).isEmpty();
  }

  @Test
  public void builder_addEdge_fromNodes() {
    // Act
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addEdge(A, B, 10).build();

    // Assert
    assertThat(network.nodes()).containsExactly(A, B);
    assertThat(network.edges()).containsExactly(10);
    assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered(A, B));
  }

  @Test
  public void builder_addEdge_fromEndpointPair() {
    // Act
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .<String, Integer>immutable()
            .addEdge(EndpointPair.ordered(A, B), 10)
            .build();

    // Assert
    assertThat(network.nodes()).containsExactly(A, B);
    assertThat(network.edges()).containsExactly(10);
    assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered(A, B));
  }

  // -------- Helper factory methods --------

  private static MutableNetwork<String, String> newDirectedStringNetworkAllowingSelfLoops() {
    return NetworkBuilder.directed().allowsSelfLoops(true).build();
  }

  private static MutableNetwork<String, String> newUndirectedStringNetworkAllowingSelfLoops() {
    return NetworkBuilder.undirected().allowsSelfLoops(true).build();
  }
}