package com.google.common.graph;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void unorderedPair_equals_isReflexive() {
        // Arrange: Create an unordered endpoint pair.
        EndpointPair<String> endpointPair = EndpointPair.unordered("A", "B");

        // Act & Assert: An object must be equal to itself, per the equals() contract.
        assertTrue("An EndpointPair should be equal to itself.", endpointPair.equals(endpointPair));
    }
}