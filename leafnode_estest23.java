package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for attribute-related functionality in {@link LeafNode} subclasses.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling attr() with the special key "#cdata" on a CDataNode
     * correctly returns the text content of that node. This key is a special
     * case for retrieving the content of text-based leaf nodes.
     */
    @Test
    public void attrWithCDataKeyReturnsNodeText() {
        // Arrange
        String expectedContent = "roS]";
        CDataNode cdataNode = new CDataNode(expectedContent);
        String cdataContentKey = "#cdata";

        // Act
        String actualContent = cdataNode.attr(cdataContentKey);

        // Assert
        assertEquals(expectedContent, actualContent);
    }
}