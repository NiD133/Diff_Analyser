package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XNode_ESTestTest86 extends XNode_ESTest_scaffolding {

    /**
     * Tests that getBooleanBody() returns null for a parent node that contains
     * only element children and no direct text content.
     */
    @Test
    public void getBooleanBodyOfParentShouldReturnNullWhenParentHasNoTextContent() {
        // Arrange
        // Create a standard parent-child DOM structure: <parent><child/></parent>.
        // The IIOMetadataNode is a concrete implementation of org.w3c.dom.Node.
        Node parentDomNode = new IIOMetadataNode("parent");
        Node childDomNode = new IIOMetadataNode("child");
        parentDomNode.appendChild(childDomNode);

        // We create an XNode for the child to test the getParent() method.
        // The XPathParser is not needed for this test, so it can be null.
        XNode childXNode = new XNode(null, childDomNode, new Properties());

        // Act
        // Retrieve the parent XNode from the child.
        XNode parentXNode = childXNode.getParent();

        // Assert
        // First, ensure the parent node was correctly retrieved.
        assertNotNull("The parent XNode should not be null.", parentXNode);

        // The main assertion: The <parent> node has no direct text content, so its
        // body is considered null. Consequently, getBooleanBody() should also return null.
        assertNull("getBooleanBody() should return null for a node with no text body.", parentXNode.getBooleanBody());
    }
}