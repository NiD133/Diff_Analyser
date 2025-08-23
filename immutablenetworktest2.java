package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest2 {

    @Test
    public void copyOfImmutableNetwork_optimized() {
        Network<String, String> network1 = ImmutableNetwork.copyOf(NetworkBuilder.directed().<String, String>build());
        Network<String, String> network2 = ImmutableNetwork.copyOf(network1);
        assertThat(network2).isSameInstanceAs(network1);
    }
}
