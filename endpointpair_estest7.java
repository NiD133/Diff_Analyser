package com.google.common.graph;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    /**
     * Verifies that creating an unordered EndpointPair with a null node is not allowed.
     * The factory method should throw a NullPointerException if any of its arguments are null,
     * as enforced by Guava's Preconditions.checkNotNull.
     */
    @Test
    public void unordered_withNullNode_throwsNullPointerException() {
        // Test case 1: The first node is null.
        assertThrows(NullPointerException.class, () -> EndpointPair.unordered(null, "nodeV"));

        // Test case 2: The second node is null.
        assertThrows(NullPointerException.class, () -> EndpointPair.unordered("nodeU", null));

        // Test case 3: Both nodes are null (the original test's scenario).
        assertThrows(NullPointerException.class, () -> EndpointPair.unordered(null, null));
    }
}