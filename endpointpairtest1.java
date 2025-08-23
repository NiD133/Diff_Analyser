package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/** Tests for {@link EndpointPair}. */
public class EndpointPairTest {

  @Test
  public void ordered_api_returnsCorrectValues() {
    // Arrange
    String sourceNode = "source";
    String targetNode = "target";

    // Act
    EndpointPair<String> orderedPair = EndpointPair.ordered(sourceNode, targetNode);

    // Assert
    // An "ordered" pair represents a directed edge.
    assertThat(orderedPair.isOrdered()).isTrue();

    // Specific accessors for ordered pairs.
    assertThat(orderedPair.source()).isEqualTo(sourceNode);
    assertThat(orderedPair.target()).isEqualTo(targetNode);

    // General accessors, which are consistent with source/target for ordered pairs.
    assertThat(orderedPair.nodeU()).isEqualTo(sourceNode);
    assertThat(orderedPair.nodeV()).isEqualTo(targetNode);

    // Behavior.
    assertThat(orderedPair.adjacentNode(sourceNode)).isEqualTo(targetNode);
    assertThat(orderedPair.adjacentNode(targetNode)).isEqualTo(sourceNode);

    // Iterable contract.
    assertThat(orderedPair).containsExactly(sourceNode, targetNode).inOrder();

    // String representation.
    assertThat(orderedPair.toString()).isEqualTo("<source -> target>");
  }
}