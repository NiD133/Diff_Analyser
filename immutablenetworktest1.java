package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest1 {

    @Test
    public void immutableNetwork() {
        MutableNetwork<String, Integer> mutableNetwork = NetworkBuilder.directed().build();
        mutableNetwork.addNode("A");
        ImmutableNetwork<String, Integer> immutableNetwork = ImmutableNetwork.copyOf(mutableNetwork);
        assertThat(immutableNetwork.asGraph()).isInstanceOf(ImmutableGraph.class);
        assertThat(immutableNetwork).isNotInstanceOf(MutableNetwork.class);
        assertThat(immutableNetwork).isEqualTo(mutableNetwork);
        mutableNetwork.addNode("B");
        assertThat(immutableNetwork).isNotEqualTo(mutableNetwork);
    }
}
