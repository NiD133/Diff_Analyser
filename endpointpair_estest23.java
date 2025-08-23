package com.google.common.graph;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    /**
     * Tests the reflexive property of the equals() method for an ordered pair.
     * An object must always be equal to itself.
     */
    @Test
    public void orderedPair_equals_isReflexive() {
        // Arrange: Create an ordered EndpointPair instance.
        // Using a simple String makes the test's intent clear.
        String node = "A";
        EndpointPair<String> pair = EndpointPair.ordered(node, node);

        // Act & Assert: The pair must be equal to itself.
        // This confirms that the equals() method correctly handles identity.
        assertTrue("An EndpointPair instance should always be equal to itself.", pair.equals(pair));
    }
}