package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ImmutableNetwork}. */
@RunWith(JUnit4.class)
public class ImmutableNetworkTest {

  @Test
  public void copyOf_fromImmutableNetwork_returnsSameInstance() {
    // Arrange: Create an initial immutable network.
    ImmutableNetwork<String, String> immutableNetwork =
        ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());

    // Act: Call copyOf() on the existing immutable network.
    // This is an optimized path that should not create a new object.
    Network<String, String> copy = ImmutableNetwork.copyOf(immutableNetwork);

    // Assert: The returned network is the same instance as the original.
    assertThat(copy).isSameInstanceAs(immutableNetwork);
  }
}