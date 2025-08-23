package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getChildren() correctly returns a list of XNode objects
     * representing the direct children of the underlying DOM node.
     */
    @Test
    public void getChildrenShouldReturnListOfChildNodes() {
        // Arrange: Create a parent DOM node with one child.
        Node parentNode = new IIOMetadataNode("parent");
        Node childNode = new IIOMetadataNode("child");
        parentNode.appendChild(childNode);

        // The XPathParser and variables are required for the XNode constructor,
        // but are not directly used by the getChildren() method in this scenario.
        Properties variables = new Properties();
        XPathParser xPathParser = new XPathParser((Document) null, false, variables);
        XNode parentXNode = new XNode(xPathParser, parentNode, variables);

        // Act: Retrieve the children from the parent XNode.
        List<XNode> children = parentXNode.getChildren();

        // Assert: Verify that the returned list contains the expected child.
        assertNotNull("The children list should not be null.", children);
        assertEquals("The parent node should have exactly one child.", 1, children.size());

        XNode resultChildXNode = children.get(0);
        assertEquals("The child node's name should match.", "child", resultChildXNode.getName());
    }
}