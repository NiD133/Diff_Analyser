package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ImmutableNetwork} and its builder. */
@RunWith(JUnit4.class)
public class ImmutableNetworkTest {

  @Test
  public void immutableBuilder_preservesPropertiesFromNetworkBuilder() {
    // This test verifies that an ImmutableNetwork, when created from a configured NetworkBuilder,
    // inherits the specified properties.

    // ARRANGE & ACT: Build an ImmutableNetwork from a NetworkBuilder with custom settings.
    ImmutableNetwork<String, Integer> network =
        NetworkBuilder.directed()
            .allowsSelfLoops(true)
            .nodeOrder(ElementOrder.<String>natural())
            .<String, Integer>immutable()
            .build();

    // ASSERT: The created network has the correct configuration.
    assertThat(network.isDirected()).isTrue();
    assertThat(network.allowsSelfLoops()).isTrue();
    assertThat(network.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
  }
}