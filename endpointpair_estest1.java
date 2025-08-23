package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link EndpointPair}. */
@RunWith(JUnit4.class)
public class EndpointPairTest {

  @Test
  public void of_undirectedNetwork_returnsUnorderedPair() {
    // Arrange: Create an undirected network and two nodes.
    final String nodeU = "A";
    final String nodeV = "B";
    Network<String, String> undirectedNetwork = NetworkBuilder.undirected().build();

    // Act: Create an EndpointPair from the network and nodes.
    EndpointPair<String> endpointPair = EndpointPair.of(undirectedNetwork, nodeU, nodeV);

    // Assert: The resulting pair should be unordered and contain the correct nodes.
    assertThat(endpointPair.isOrdered()).isFalse();
    assertThat(ImmutableSet.copyOf(endpointPair)).containsExactly(nodeU, nodeV);

    // For an unordered pair, source() and target() are not defined.
    assertThrows(UnsupportedOperationException.class, endpointPair::source);
    assertThrows(UnsupportedOperationException.class, endpointPair::target);
  }

  @Test
  public void of_directedNetwork_returnsOrderedPair() {
    // Arrange: Create a directed network and two nodes representing a source and a target.
    final String source = "S";
    final String target = "T";
    Network<String, String> directedNetwork = NetworkBuilder.directed().build();

    // Act: Create an EndpointPair from the network and nodes.
    EndpointPair<String> endpointPair = EndpointPair.of(directedNetwork, source, target);

    // Assert: The resulting pair should be ordered and have the correct source and target.
    assertThat(endpointPair.isOrdered()).isTrue();
    assertThat(endpointPair.source()).isEqualTo(source);
    assertThat(endpointPair.target()).isEqualTo(target);
  }
}