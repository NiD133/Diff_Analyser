package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link ImmutableNetwork}. */
@RunWith(JUnit4.class)
@NullUnmarked
public class ImmutableNetworkTest {

  /** Tests that an ImmutableNetwork is correctly created from a MutableNetwork. */
  @Test
  public void testImmutableNetworkCreation() {
    // Create a directed mutable network and add a node
    MutableNetwork<String, Integer> mutableNetwork = NetworkBuilder.directed().build();
    mutableNetwork.addNode("A");

    // Create an immutable network from the mutable network
    ImmutableNetwork<String, Integer> immutableNetwork = ImmutableNetwork.copyOf(mutableNetwork);

    // Verify the immutable network is an instance of ImmutableGraph and not MutableNetwork
    assertThat(immutableNetwork.asGraph()).isInstanceOf(ImmutableGraph.class);
    assertThat(immutableNetwork).isNotInstanceOf(MutableNetwork.class);

    // Verify the immutable network is equal to the original mutable network
    assertThat(immutableNetwork).isEqualTo(mutableNetwork);

    // Add another node to the mutable network and verify the immutable network remains unchanged
    mutableNetwork.addNode("B");
    assertThat(immutableNetwork).isNotEqualTo(mutableNetwork);
  }

  /** Tests optimization when copying an already immutable network. */
  @Test
  public void testCopyOfImmutableNetworkOptimization() {
    // Create an immutable network
    Network<String, String> originalNetwork =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());

    // Copy the immutable network
    Network<String, String> copiedNetwork = ImmutableNetwork.copyOf(originalNetwork);

    // Verify that the copied network is the same instance as the original
    assertThat(copiedNetwork).isSameInstanceAs(originalNetwork);
  }

  /** Tests edges connecting nodes in a directed network. */
  @Test
  public void testEdgesConnectingInDirectedNetwork() {
    // Create a directed mutable network allowing self-loops
    MutableNetwork<String, String> mutableNetwork =
        NetworkBuilder.directed().allowsSelfLoops(true).build();
    mutableNetwork.addEdge("A", "A", "AA");
    mutableNetwork.addEdge("A", "B", "AB");

    // Create an immutable network from the mutable network
    Network<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);

    // Verify edges connecting specific nodes
    assertThat(network.edgesConnecting("A", "A")).containsExactly("AA");
    assertThat(network.edgesConnecting("A", "B")).containsExactly("AB");
    assertThat(network.edgesConnecting("B", "A")).isEmpty();
  }

  /** Tests edges connecting nodes in an undirected network. */
  @Test
  public void testEdgesConnectingInUndirectedNetwork() {
    // Create an undirected mutable network allowing self-loops
    MutableNetwork<String, String> mutableNetwork =
        NetworkBuilder.undirected().allowsSelfLoops(true).build();
    mutableNetwork.addEdge("A", "A", "AA");
    mutableNetwork.addEdge("A", "B", "AB");

    // Create an immutable network from the mutable network
    Network<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);

    // Verify edges connecting specific nodes
    assertThat(network.edgesConnecting("A", "A")).containsExactly("AA");
    assertThat(network.edgesConnecting("A", "B")).containsExactly("AB");
    assertThat(network.edgesConnecting("B", "A")).containsExactly("AB");
  }

  /** Tests that the ImmutableNetwork.Builder applies the NetworkBuilder configuration. */
  @Test
  public void testImmutableNetworkBuilderAppliesConfiguration() {
    // Create an immutable network with specific configuration
    ImmutableNetwork<String, Integer> emptyNetwork =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural())
            .<String, Integer>immutable()
            .build();

    // Verify the configuration of the immutable network
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  /** Tests that the ImmutableNetwork.Builder is unaffected by changes to the original NetworkBuilder. */
  @Test
  @SuppressWarnings("CheckReturnValue")
  public void testImmutableNetworkBuilderCopiesNetworkBuilder() {
    // Create a network builder with specific configuration
    NetworkBuilder<String, Object> networkBuilder =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .<String>nodeOrder(ElementOrder.<String>natural());

    // Create an immutable network builder from the network builder
    ImmutableNetwork.Builder<String, Integer> immutableNetworkBuilder =
        networkBuilder.<String, Integer>immutable();

    // Modify the original network builder
    networkBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.<String>unordered());

    // Build an immutable network from the immutable network builder
    ImmutableNetwork<String, Integer> emptyNetwork = immutableNetworkBuilder.build();

    // Verify that the immutable network retains the original configuration
    assertThat(emptyNetwork.isDirected()).isTrue();
    assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
    assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }

  /** Tests adding a node using the ImmutableNetwork.Builder. */
  @Test
  public void testImmutableNetworkBuilderAddNode() {
    // Create an immutable network with a single node
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addNode("A").build();

    // Verify the network contains the node and no edges
    assertThat(network.nodes()).containsExactly("A");
    assertThat(network.edges()).isEmpty();
  }

  /** Tests adding an edge between nodes using the ImmutableNetwork.Builder. */
  @Test
  public void testImmutableNetworkBuilderAddEdgeFromNodes() {
    // Create an immutable network with an edge between two nodes
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed().<String, Integer>immutable().addEdge("A", "B", 10).build();

    // Verify the network contains the nodes and the edge
    assertThat(network.nodes()).containsExactly("A", "B");
    assertThat(network.edges()).containsExactly(10);
    assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered("A", "B"));
  }

  /** Tests adding an edge using an EndpointPair with the ImmutableNetwork.Builder. */
  @Test
  public void testImmutableNetworkBuilderAddEdgeFromEndpointPair() {
    // Create an immutable network with an edge using an EndpointPair
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .<String, Integer>immutable()
            .addEdge(EndpointPair.ordered("A", "B"), 10)
            .build();

    // Verify the network contains the nodes and the edge
    assertThat(network.nodes()).containsExactly("A", "B");
    assertThat(network.edges()).containsExactly(10);
    assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered("A", "B"));
  }
}