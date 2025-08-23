package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class ImmutableNetworkTestTest6 {

    /**
     * Tests that the ImmutableNetwork.Builder doesn't change when the creating NetworkBuilder
     * changes.
     */
    @Test
    @SuppressWarnings("CheckReturnValue")
    public void immutableNetworkBuilder_copiesNetworkBuilder() {
        NetworkBuilder<String, Object> networkBuilder = NetworkBuilder.directed().allowsSelfLoops(true).<String>nodeOrder(ElementOrder.<String>natural());
        ImmutableNetwork.Builder<String, Integer> immutableNetworkBuilder = networkBuilder.<String, Integer>immutable();
        // Update NetworkBuilder, but this shouldn't impact immutableNetworkBuilder
        networkBuilder.allowsSelfLoops(false).nodeOrder(ElementOrder.<String>unordered());
        ImmutableNetwork<String, Integer> emptyNetwork = immutableNetworkBuilder.build();
        assertThat(emptyNetwork.isDirected()).isTrue();
        assertThat(emptyNetwork.allowsSelfLoops()).isTrue();
        assertThat(emptyNetwork.nodeOrder()).isEqualTo(ElementOrder.<String>natural());
    }
}
