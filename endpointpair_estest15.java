package com.google.common.graph;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair#equals(Object)}.
 */
public class EndpointPairTest {

    @Test
    public void unorderedPairsWithDifferentNodes_areNotEqual() {
        // Arrange: Create two unordered pairs that share one node but differ in the other.
        // Using simple, descriptive strings as nodes makes the test's purpose clear,
        // unlike the original's use of arbitrary objects like Locale.Category.
        EndpointPair<String> pairAB = EndpointPair.unordered("A", "B");
        EndpointPair<String> pairAC = EndpointPair.unordered("A", "C");

        // Act & Assert: The two pairs should not be equal because their sets of nodes
        // are different ({A, B} is not equal to {A, C}).
        assertFalse("Pairs with different nodes should not be equal.", pairAB.equals(pairAC));
    }
}