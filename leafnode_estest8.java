package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the LeafNode class, focusing on attribute-related behavior.
 */
public class LeafNodeTest {

    /**
     * Verifies that a CDataNode, a type of LeafNode, correctly reports
     * having an implicit attribute that represents its text content.
     * The key for this implicit attribute is the node's name, "#cdata".
     */
    @Test
    public void hasAttrOnCDataNodeReturnsTrueForImplicitTextAttribute() {
        // Arrange: Create a CDataNode. Its content doesn't matter for this check.
        CDataNode cdataNode = new CDataNode("some cdata content");
        String implicitAttributeKey = "#cdata";

        // Act: Check if the node has the implicit attribute.
        boolean hasAttribute = cdataNode.hasAttr(implicitAttributeKey);

        // Assert: The node should always report having this attribute.
        assertTrue("A CDataNode should always have an implicit '#cdata' attribute representing its content.", hasAttribute);
    }
}