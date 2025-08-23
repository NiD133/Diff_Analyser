package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest5 {

    @Test
    public void immutableNetworkBuilder_appliesNetworkBuilderConfig() {
        ImmutableNetwork<String, Integer> emptyNetwork = NetworkBuilder.directed().allowsSelfLoops(true).nodeOrder(ElementOrder.<String>natural()).<String, Integer>immutable().build();
        assertThat(emptyNetwork.isDirected()).isTrue();
        assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
        assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
    }
}
