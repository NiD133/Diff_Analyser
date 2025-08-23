package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getParent() returns null when the underlying DOM node has no parent.
     */
    @Test
    public void getParentShouldReturnNullForNodeWithoutParent() {
        // Arrange: Create an XNode that wraps a root-level DOM node.
        // IIOMetadataNode is a convenient concrete implementation of org.w3c.dom.Node
        // that can be instantiated directly.
        Node rootNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, rootNode, variables);

        // Act: Attempt to retrieve the parent node.
        XNode parentNode = xNode.getParent();

        // Assert: The parent should be null, as the underlying node is a root element.
        assertNull("The parent of an XNode wrapping a root DOM node should be null.", parentNode);
    }
}