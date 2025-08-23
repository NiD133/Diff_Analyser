package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@NullUnmarked
public class ImmutableNetworkTestTest4 {

  private static final String NODE_A = "A";
  private static final String NODE_B = "B";
  private static final String EDGE_AA_SELF_LOOP = "AA";
  private static final String EDGE_AB = "AB";

  @Test
  public void edgesConnecting_inUndirectedNetwork_returnsAllEdgesBetweenNodes() {
    // Arrange: Create an undirected immutable network with a self-loop edge
    // and a regular edge using the dedicated builder.
    ImmutableNetwork<String, String> network =
        NetworkBuilder.undirected()
            .allowsSelfLoops(true)
            .<String, String>immutable()
            .addEdge(NODE_A, NODE_A, EDGE_AA_SELF_LOOP)
            .addEdge(NODE_A, NODE_B, EDGE_AB)
            .build();

    // Assert: Verify that edgesConnecting() returns the correct edges for each case.

    // Case 1: A self-loop edge should be returned when source and target are the same.
    assertThat(network.edgesConnecting(NODE_A, NODE_A)).containsExactly(EDGE_AA_SELF_LOOP);

    // Case 2: An edge between two distinct nodes.
    assertThat(network.edgesConnecting(NODE_A, NODE_B)).containsExactly(EDGE_AB);

    // Case 3: In an undirected network, the edge is the same when querying in reverse.
    assertThat(network.edgesConnecting(NODE_B, NODE_A)).containsExactly(EDGE_AB);
  }
}