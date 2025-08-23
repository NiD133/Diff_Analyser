package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest7 {

    @Test
    public void immutableNetworkBuilder_addNode() {
        ImmutableNetwork<String, Integer> network = NetworkBuilder.directed().<String, Integer>immutable().addNode("A").build();
        assertThat(network.nodes()).containsExactly("A");
        assertThat(network.edges()).isEmpty();
    }
}
