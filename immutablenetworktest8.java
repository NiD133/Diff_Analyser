package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest8 {

    @Test
    public void immutableNetworkBuilder_putEdgeFromNodes() {
        ImmutableNetwork<String, Integer> network = NetworkBuilder.directed().<String, Integer>immutable().addEdge("A", "B", 10).build();
        assertThat(network.nodes()).containsExactly("A", "B");
        assertThat(network.edges()).containsExactly(10);
        assertThat(network.incidentNodes(10)).isEqualTo(EndpointPair.ordered("A", "B"));
    }
}
