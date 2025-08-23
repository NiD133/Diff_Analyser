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

public class EndpointPairTestTest12 {

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
    public void endpointPair_directed_contains() {
        MutableGraph<Integer> directedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
        directedGraph.putEdge(N1, N1);
        directedGraph.putEdge(N1, N2);
        Set<EndpointPair<Integer>> edges = directedGraph.edges();
        assertThat(edges).hasSize(2);
        assertThat(edges).contains(EndpointPair.ordered(N1, N1));
        assertThat(edges).contains(EndpointPair.ordered(N1, N2));
        // unordered endpoints not OK for directed graph (undefined behavior)
        assertThat(edges).doesNotContain(EndpointPair.unordered(N1, N2));
        // wrong order
        assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N1));
        // edge not present
        assertThat(edges).doesNotContain(EndpointPair.ordered(N2, N2));
        // nodes not in graph
        assertThat(edges).doesNotContain(EndpointPair.ordered(N3, N4));
    }
}
