package com.google.common.graph;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the factory methods of {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void ordered_whenSourceIsNull_throwsNullPointerException() {
        // The source node for an ordered pair is non-nullable.
        assertThrows(NullPointerException.class, () -> EndpointPair.ordered(null, "nodeV"));
    }

    @Test
    public void ordered_whenTargetIsNull_throwsNullPointerException() {
        // The target node for an ordered pair is non-nullable.
        assertThrows(NullPointerException.class, () -> EndpointPair.ordered("nodeU", null));
    }
}