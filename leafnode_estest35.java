package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the LeafNode abstract class, using TextNode as a concrete implementation.
 */
public class LeafNodeTest {

    /**
     * Verifies that a LeafNode correctly inherits its base URI from its parent node.
     */
    @Test
    public void baseUriShouldReturnParentBaseUriWhenParentIsSet() {
        // Arrange: Create a parent document with a defined base URI and a child LeafNode.
        final String expectedBaseUri = "https://jsoup.org/docs/";
        Document parentDocument = new Document(expectedBaseUri);
        TextNode textNode = new TextNode("This is a leaf node.");

        // Use the public API to establish the parent-child relationship.
        parentDocument.body().appendChild(textNode);

        // Act: Call the method under test.
        String actualBaseUri = textNode.baseUri();

        // Assert: The LeafNode's base URI should match its parent's.
        assertEquals("The base URI should be inherited from the parent document.", expectedBaseUri, actualBaseUri);
    }
}