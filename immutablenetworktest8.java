package com.google.common.graph;

import static com.google.common.graph.EndpointPair.ordered;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ImmutableNetwork}. */
@RunWith(JUnit4.class)
public class ImmutableNetworkTest {

  private static final String NODE_A = "A";
  private static final String NODE_B = "B";
  private static final Integer EDGE_A_B = 10;

  @Test
  public void addEdge_directed_addsNodesAndEdge() {
    // ARRANGE: Start building a directed immutable network.
    // ACT: Add an edge, which should also implicitly add the nodes.
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .<String, Integer>immutable()
            .addEdge(NODE_A, NODE_B, EDGE_A_B)
            .build();

    // ASSERT: The resulting network should contain the new nodes and the directed edge.
    assertThat(network.nodes()).containsExactly(NODE_A, NODE_B);
    assertThat(network.edges()).containsExactly(EDGE_A_B);
    assertThat(network.incidentNodes(EDGE_A_B)).isEqualTo(ordered(NODE_A, NODE_B));
  }
}