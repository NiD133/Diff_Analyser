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

public class EndpointPairTestTest10 {

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
    public void endpointPair_unmodifiableView() {
        MutableGraph<Integer> directedGraph = GraphBuilder.directed().build();
        Set<EndpointPair<Integer>> edges = directedGraph.edges();
        directedGraph.putEdge(N1, N2);
        containsExactlySanityCheck(edges, EndpointPair.ordered(N1, N2));
        directedGraph.putEdge(N2, N1);
        containsExactlySanityCheck(edges, EndpointPair.ordered(N1, N2), EndpointPair.ordered(N2, N1));
        directedGraph.removeEdge(N1, N2);
        directedGraph.removeEdge(N2, N1);
        containsExactlySanityCheck(edges);
        assertThrows(UnsupportedOperationException.class, () -> edges.add(EndpointPair.ordered(N1, N2)));
    }
}
