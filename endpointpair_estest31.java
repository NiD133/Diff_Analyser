package com.google.common.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void unordered_equals_isOrderIndependent() {
        // Arrange: Define two distinct nodes for the endpoint pair.
        String nodeU = "U";
        String nodeV = "V";

        // Act: Create two unordered EndpointPairs with the same nodes but in opposite order.
        EndpointPair<String> pair1 = EndpointPair.unordered(nodeU, nodeV);
        EndpointPair<String> pair2 = EndpointPair.unordered(nodeV, nodeU);

        // Assert: The two pairs should be considered equal, as node order doesn't matter
        // for unordered pairs. The hashCode must also be consistent with equals.
        assertEquals(pair1, pair2);
        assertEquals("Hash codes must be equal for equal objects.", pair1.hashCode(), pair2.hashCode());
    }
}