package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest3 {

    @Test
    public void edgesConnecting_directed() {
        MutableNetwork<String, String> mutableNetwork = NetworkBuilder.directed().allowsSelfLoops(true).build();
        mutableNetwork.addEdge("A", "A", "AA");
        mutableNetwork.addEdge("A", "B", "AB");
        Network<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);
        assertThat(network.edgesConnecting("A", "A")).containsExactly("AA");
        assertThat(network.edgesConnecting("A", "B")).containsExactly("AB");
        assertThat(network.edgesConnecting("B", "A")).isEmpty();
    }
}
