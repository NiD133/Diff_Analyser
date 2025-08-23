package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for attribute-related methods in the {@link LeafNode} class.
 */
public class LeafNodeTest {

    @Test
    public void hasAttrShouldReturnFalseWhenAttributeDoesNotExist() {
        // Arrange: Create a LeafNode instance (a Comment) that has no attributes by default.
        LeafNode node = new Comment("A sample comment");

        // Act & Assert: Verify that hasAttr returns false for an attribute that has not been set.
        boolean hasAttribute = node.hasAttr("nonexistent-attribute");
        
        assertFalse("A new leaf node should not report having an attribute.", hasAttribute);
    }
}