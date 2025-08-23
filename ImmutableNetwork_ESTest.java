package com.google.common.graph;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ImmutableNetworkTest {

  @Test
  public void copyOf_withNullNetwork_throwsNPE() {
    assertThrows(NullPointerException.class, () -> ImmutableNetwork.copyOf((Network<Integer, Integer>) null));
  }

  @Test
  public void copyOf_withNullImmutableNetwork_throwsNPE() {
    assertThrows(NullPointerException.class, () -> ImmutableNetwork.copyOf((ImmutableNetwork<Integer, Integer>) null));
  }

  @Test
  public void copyOf_givenImmutableNetwork_returnsSameInstance_forNetworkOverload() {
    ImmutableNetwork<Integer, String> immutable =
        NetworkBuilder.directed().<Integer, String>immutable().build();

    ImmutableNetwork<Integer, String> result = ImmutableNetwork.copyOf((Network<Integer, String>) immutable);

    assertSame(immutable, result);
  }

  @Test
  public void copyOf_givenImmutableNetwork_returnsSameInstance_forDeprecatedOverload() {
    ImmutableNetwork<Integer, String> immutable =
        NetworkBuilder.directed().<Integer, String>immutable().build();

    ImmutableNetwork<Integer, String> result = ImmutableNetwork.copyOf(immutable);

    assertSame(immutable, result);
  }

  @Test
  public void builder_addNode_andBuild_containsNode() {
    ImmutableNetwork<Integer, String> network =
        NetworkBuilder.directed()
            .<Integer, String>immutable()
            .addNode(1)
            .build();

    assertTrue(network.nodes().contains(1));
    assertTrue(network.edges().isEmpty());
  }

  @Test
  public void builder_addEdgeByNodes_addsNodesImplicitly_andCreatesEdge() {
    Integer a = 1;
    Integer b = 2;
    String edge = "e1";

    ImmutableNetwork<Integer, String> network =
        NetworkBuilder.directed()
            .<Integer, String>immutable()
            .addEdge(a, b, edge)
            .build();

    assertTrue(network.nodes().containsAll(Arrays.asList(a, b)));
    assertTrue(network.edges().contains(edge));
    assertEquals(EndpointPair.ordered(a, b), network.incidentNodes(edge));
  }

  @Test
  public void builder_addEdgeByEndpoints_inUndirectedNetwork() {
    Integer a = 1;
    Integer b = 2;
    String edge = "e1";
    EndpointPair<Integer> endpoints = EndpointPair.unordered(a, b);

    ImmutableNetwork<Integer, String> network =
        NetworkBuilder.undirected()
            .<Integer, String>immutable()
            .addEdge(endpoints, edge)
            .build();

    assertTrue(network.nodes().containsAll(Arrays.asList(a, b)));
    assertTrue(network.edges().contains(edge));
    assertEquals(endpoints, network.incidentNodes(edge));
  }

  @Test
  public void asGraph_returnsImmutableGraphView() {
    ImmutableNetwork<Integer, String> network =
        NetworkBuilder.directed().<Integer, String>immutable().build();

    ImmutableGraph<Integer> graphView = network.asGraph();

    assertNotNull(graphView);
    assertTrue(graphView.isDirected());
    assertEquals(network.nodes(), graphView.nodes());
  }
}