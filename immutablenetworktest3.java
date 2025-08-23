package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ImmutableNetwork}. */
@RunWith(JUnit4.class)
public class ImmutableNetworkTest {

  private static final String NODE_A = "A";
  private static final String NODE_B = "B";
  private static final String EDGE_SELF_LOOP = "A->A";
  private static final String EDGE_A_TO_B = "A->B";

  @Test
  public void edgesConnecting_inDirectedNetwork() {
    // ARRANGE: Create a directed immutable network with a self-loop and a standard edge.
    ImmutableNetwork<String, String> network =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .<String, String>immutable()
            .addEdge(NODE_A, NODE_A, EDGE_SELF_LOOP)
            .addEdge(NODE_A, NODE_B, EDGE_A_TO_B)
            .build();

    // ACT & ASSERT

    // A self-loop edge should be found when querying its source/target node.
    assertThat(network.edgesConnecting(NODE_A, NODE_A)).containsExactly(EDGE_SELF_LOOP);

    // A standard directed edge should be found.
    assertThat(network.edgesConnecting(NODE_A, NODE_B)).containsExactly(EDGE_A_TO_B);

    // The reverse edge should not exist in a directed network if not explicitly added.
    assertThat(network.edgesConnecting(NODE_B, NODE_A)).isEmpty();
  }
}