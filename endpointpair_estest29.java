package com.google.common.graph;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    /**
     * Verifies that an ordered EndpointPair is never equal to an unordered one,
     * even if they contain the same nodes, as specified by the equals() contract.
     */
    @Test
    public void equals_returnsFalse_whenComparingOrderedAndUnorderedPairs() {
        // Arrange: Create an ordered and an unordered pair with the same nodes.
        String nodeU = "U";
        String nodeV = "V";

        EndpointPair<String> orderedPair = EndpointPair.ordered(nodeU, nodeV);
        EndpointPair<String> unorderedPair = EndpointPair.unordered(nodeU, nodeV);

        // Assert: Per the Javadoc, an ordered pair can never be equal to an unordered one.
        // We use assertNotEquals to clearly state this expectation.
        assertNotEquals(orderedPair, unorderedPair);
        assertNotEquals(unorderedPair, orderedPair); // Also test the reverse comparison
    }
}