package com.google.common.graph;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test(expected = NullPointerException.class)
    public void adjacentNode_withNullArgument_throwsNullPointerException() {
        // Arrange: Create an endpoint pair. The specific nodes don't matter for this test.
        EndpointPair<Integer> endpointPair = EndpointPair.unordered(1, 2);

        // Act: Call the method under test with a null argument.
        // The @Test(expected=...) annotation handles the assertion.
        endpointPair.adjacentNode(null);
    }
}