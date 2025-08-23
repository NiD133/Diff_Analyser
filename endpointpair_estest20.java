package com.google.common.graph;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void equals_orderedPairsWithDifferentNodes_shouldReturnFalse() {
        // Arrange: Create two distinct ordered endpoint pairs.
        // The equals() method for ordered pairs requires both the source and target nodes
        // to be equal in the same order.
        EndpointPair<Integer> pairA = EndpointPair.ordered(1, 2);
        EndpointPair<Integer> pairB = EndpointPair.ordered(3, 4);

        // Act & Assert: Verify that the two distinct pairs are not equal.
        assertNotEquals(pairA, pairB);
    }
}