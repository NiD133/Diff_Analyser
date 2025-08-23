package com.google.common.graph;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void equals_whenComparedWithDifferentObjectType_returnsFalse() {
        // Arrange
        // An EndpointPair can be created directly without instantiating a full graph.
        // We use EndpointPair.ordered() to match the directed graph behavior in the original test.
        EndpointPair<Integer> endpointPair = EndpointPair.ordered(1, 2);
        Object otherObject = new Object();

        // Act
        boolean isEqual = endpointPair.equals(otherObject);

        // Assert
        assertFalse("An EndpointPair instance should not be equal to an object of a different type.", isEqual);
    }
}