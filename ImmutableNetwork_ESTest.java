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
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ImmutableNetwork}.
 */
@RunWith(JUnit4.class)
public class ImmutableNetworkTest {

    @Test(expected = NullPointerException.class)
    public void copyOf_network_withNull_throwsNullPointerException() {
        ImmutableNetwork.copyOf((Network<String, String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void copyOf_immutableNetwork_withNull_throwsNullPointerException() {
        // This overload is deprecated, but we still test its contract.
        ImmutableNetwork.copyOf((ImmutableNetwork<String, String>) null);
    }

    @Test
    public void copyOf_network_whenAlreadyImmutable_returnsSameInstance() {
        // Arrange
        ImmutableNetwork<String, Integer> immutableNetwork =
            NetworkBuilder.directed().<String, Integer>immutable().addNode("A").build();

        // Act & Assert: copyOf(Network) should recognize it's already immutable and not create a new instance.
        assertSame(immutableNetwork, ImmutableNetwork.copyOf(immutableNetwork));
    }

    @Test
    @SuppressWarnings("deprecation") // Testing a deprecated method on purpose.
    public void copyOf_immutableNetwork_returnsSameInstance() {
        // Arrange
        ImmutableNetwork<String, Integer> immutableNetwork =
            NetworkBuilder.directed().<String, Integer>immutable().addNode("A").build();

        // Act & Assert: The deprecated copyOf(ImmutableNetwork) should be a no-op.
        assertSame(immutableNetwork, ImmutableNetwork.copyOf(immutableNetwork));
    }

    @Test
    public void copyOf_mutableNetwork_createsCorrectImmutableCopy() {
        // Arrange
        MutableNetwork<String, String> mutableNetwork = NetworkBuilder.undirected().allowsParallelEdges(true).build();
        mutableNetwork.addEdge("A", "B", "AB-1");
        mutableNetwork.addEdge("B", "C", "BC-1");
        mutableNetwork.addNode("D");

        // Act
        ImmutableNetwork<String, String> immutableCopy = ImmutableNetwork.copyOf(mutableNetwork);

        // Assert
        assertThat(immutableCopy.nodes()).containsExactly("A", "B", "C", "D");
        assertThat(immutableCopy.edges()).containsExactly("AB-1", "BC-1");
        assertThat(immutableCopy.incidentEdges("B")).containsExactly("AB-1", "BC-1");
        assertThat(immutableCopy.isDirected()).isFalse();
        assertThat(immutableCopy.allowsParallelEdges()).isTrue();
        assertThat(immutableCopy.allowsSelfLoops()).isFalse();

        // Verify it's a true copy by modifying the original network.
        mutableNetwork.addEdge("C", "D", "CD-1");
        assertThat(immutableCopy.edges()).doesNotContain("CD-1");
    }

    @Test
    public void builder_addNode_buildsNetworkWithNode() {
        // Arrange
        String nodeA = "A";

        // Act
        ImmutableNetwork<String, Integer> network =
            NetworkBuilder.directed().<String, Integer>immutable().addNode(nodeA).build();

        // Assert
        assertThat(network.nodes()).containsExactly(nodeA);
        assertThat(network.edges()).isEmpty();
    }

    @Test
    public void builder_addEdge_buildsNetworkWithEdge() {
        // Arrange
        String nodeU = "U";
        String nodeV = "V";
        String edgeUV = "UV-edge";

        // Act
        ImmutableNetwork<String, String> network =
            NetworkBuilder.directed()
                .<String, String>immutable()
                .addEdge(nodeU, nodeV, edgeUV)
                .build();

        // Assert
        assertThat(network.nodes()).containsExactly(nodeU, nodeV);
        assertThat(network.edges()).containsExactly(edgeUV);
        assertThat(network.incidentNodes(edgeUV)).isEqualTo(EndpointPair.ordered(nodeU, nodeV));
    }

    @Test
    public void builder_addEdgeWithEndpointPair_buildsNetworkWithEdge() {
        // Arrange
        String nodeU = "U";
        String nodeV = "V";
        String edgeUV = "UV-edge";
        EndpointPair<String> endpoints = EndpointPair.undirected(nodeU, nodeV);

        // Act
        ImmutableNetwork<String, String> network =
            NetworkBuilder.undirected()
                .<String, String>immutable()
                .addEdge(endpoints, edgeUV)
                .build();

        // Assert
        assertThat(network.nodes()).containsExactly(nodeU, nodeV);
        assertThat(network.edges()).containsExactly(edgeUV);
        assertThat(network.incidentNodes(edgeUV)).isEqualTo(endpoints);
    }

    @Test
    public void asGraph_returnsGraphWithSameTopology() {
        // Arrange
        ImmutableNetwork<String, Integer> network =
            NetworkBuilder.directed()
                .<String, Integer>immutable()
                .addEdge("A", "B", 1)
                .addEdge("B", "C", 2)
                .addNode("D") // An isolated node
                .build();

        // Act
        Graph<String> graphView = network.asGraph();

        // Assert
        assertThat(graphView.nodes()).isEqualTo(network.nodes());
        assertThat(graphView.edges()).hasSize(2);
        assertThat(graphView.hasEdgeConnecting("A", "B")).isTrue();
        assertThat(graphView.hasEdgeConnecting("B", "C")).isTrue();
        assertThat(graphView.successors("A")).containsExactly("B");
        assertThat(graphView.degree("D")).isEqualTo(0);
        assertThat(graphView.isDirected()).isTrue();
    }
}