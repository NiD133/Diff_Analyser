package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    @Test
    public void source_onUnorderedPair_throwsUnsupportedOperationException() {
        // Arrange: Create an unordered pair, which represents an edge in an undirected graph.
        // Source and target are undefined for unordered pairs.
        EndpointPair<String> unorderedPair = EndpointPair.unordered("nodeU", "nodeV");

        // Act & Assert: Verify that calling source() throws an exception with a helpful message.
        try {
            unorderedPair.source();
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException expected) {
            assertEquals(
                "Cannot call source()/target() on a EndpointPair from an undirected graph. "
                    + "Consider calling adjacentNode(node) if you already have a node, "
                    + "or nodeU()/nodeV() if you don't.",
                expected.getMessage());
        }
    }
}