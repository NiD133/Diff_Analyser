package com.google.common.graph;

import org.junit.Test;

/**
 * Contains tests for the {@link EndpointPair} class, focusing on its factory methods.
 */
public class EndpointPairTest {

    /**
     * Tests that creating an ordered EndpointPair with null arguments
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void ordered_withNullArguments_shouldThrowNullPointerException() {
        EndpointPair.ordered(null, null);
    }
}