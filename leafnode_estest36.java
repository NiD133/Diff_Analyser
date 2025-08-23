package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the abstract LeafNode class.
 */
public class LeafNodeTest {

    /**
     * Verifies that a newly created LeafNode has an empty base URI by default.
     */
    @Test
    public void newLeafNodeHasEmptyBaseUri() {
        // Arrange: Create a concrete instance of a LeafNode.
        // CDataNode is used here as a representative subclass.
        LeafNode node = new CDataNode("some cdata content");

        // Act: Retrieve the base URI from the node.
        String baseUri = node.baseUri();

        // Assert: The base URI should be an empty string, as it has not been set.
        assertEquals("", baseUri);
    }
}