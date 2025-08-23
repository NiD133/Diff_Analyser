package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link LeafNode} abstract class.
 * Since LeafNode is abstract, a concrete subclass like {@link Comment} is used for instantiation.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling absUrl() for an attribute that does not exist on a LeafNode
     * returns an empty string.
     */
    @Test
    public void absUrlOfNonExistentAttributeShouldReturnEmptyString() {
        // Arrange: Create a LeafNode instance (a Comment) that has no attributes.
        LeafNode leafNode = new Comment("This is a comment");
        String nonExistentAttributeKey = "href";

        // Act: Attempt to get the absolute URL of an attribute that isn't set.
        String absoluteUrl = leafNode.absUrl(nonExistentAttributeKey);

        // Assert: The result should be an empty string, as the attribute does not exist.
        assertEquals("", absoluteUrl);
    }
}