package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest4 {

    @Test
    public void edgesConnecting_undirected() {
        MutableNetwork<String, String> mutableNetwork = NetworkBuilder.undirected().allowsSelfLoops(true).build();
        mutableNetwork.addEdge("A", "A", "AA");
        mutableNetwork.addEdge("A", "B", "AB");
        Network<String, String> network = ImmutableNetwork.copyOf(mutableNetwork);
        assertThat(network.edgesConnecting("A", "A")).containsExactly("AA");
        assertThat(network.edgesConnecting("A", "B")).containsExactly("AB");
        assertThat(network.edgesConnecting("B", "A")).containsExactly("AB");
    }
}
