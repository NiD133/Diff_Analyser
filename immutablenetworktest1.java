package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/** Tests for {@link ImmutableNetwork}. */
public class ImmutableNetworkTest {

  @Test
  public void copyOf_createsEquivalentNetwork() {
    // Arrange: Create a mutable network with some elements.
    MutableNetwork<String, Integer> sourceNetwork = NetworkBuilder.directed().build();
    sourceNetwork.addNode("A");
    sourceNetwork.addNode("B");
    sourceNetwork.addEdge("A", "B", 1);

    // Act: Create an immutable copy.
    ImmutableNetwork<String, Integer> immutableCopy = ImmutableNetwork.copyOf(sourceNetwork);

    // Assert: The copy is structurally equivalent to the original network.
    assertThat(immutableCopy).isEqualTo(sourceNetwork);
  }

  @Test
  public void copyOf_isUnaffectedBySourceMutation() {
    // Arrange: Create a mutable network and an immutable copy of it.
    MutableNetwork<String, Integer> sourceNetwork = NetworkBuilder.directed().build();
    sourceNetwork.addNode("A");
    ImmutableNetwork<String, Integer> immutableCopy = ImmutableNetwork.copyOf(sourceNetwork);

    // Act: Modify the original network *after* the copy is made.
    sourceNetwork.addNode("B");

    // Assert: The immutable copy remains unchanged and is no longer equal to the mutated source.
    assertThat(immutableCopy.nodes()).doesNotContain("B");
    assertThat(immutableCopy).isNotEqualTo(sourceNetwork);
  }

  @Test
  public void immutableNetwork_usesImmutableComponents() {
    // Arrange: Create an empty immutable network.
    ImmutableNetwork<String, Integer> immutableNetwork =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, Integer>build());

    // Assert: The network and its graph view are of immutable types.
    assertThat(immutableNetwork).isNotInstanceOf(MutableNetwork.class);
    assertThat(immutableNetwork.asGraph()).isInstanceOf(ImmutableGraph.class);
  }
}