package com.google.common.graph;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import java.util.Collection;
import java.util.Set;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class EndpointPairTestTest9 {

    private static final Integer N0 = 0;

    private static final Integer N1 = 1;

    private static final Integer N2 = 2;

    private static final Integer N3 = 3;

    private static final Integer N4 = 4;

    private static final String E12 = "1-2";

    private static final String E12_A = "1-2a";

    private static final String E21 = "2-1";

    private static final String E13 = "1-3";

    private static final String E44 = "4-4";

    private static void containsExactlySanityCheck(Collection<?> collection, Object... varargs) {
        assertThat(collection).hasSize(varargs.length);
        for (Object obj : varargs) {
            assertThat(collection).contains(obj);
        }
        assertThat(collection).containsExactly(varargs);
    }

    @Test
    public void endpointPair_undirectedNetwork() {
        MutableNetwork<Integer, String> undirectedNetwork = NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build();
        undirectedNetwork.addNode(N0);
        undirectedNetwork.addEdge(N1, N2, E12);
        // adds parallel edge, won't be in Graph edges
        undirectedNetwork.addEdge(N2, N1, E12_A);
        undirectedNetwork.addEdge(N1, N3, E13);
        undirectedNetwork.addEdge(N4, N4, E44);
        containsExactlySanityCheck(undirectedNetwork.asGraph().edges(), EndpointPair.unordered(N1, N2), EndpointPair.unordered(N1, N3), EndpointPair.unordered(N4, N4));
    }
}
