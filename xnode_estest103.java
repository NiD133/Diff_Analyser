package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XNode} class, focusing on path resolution.
 */
public class XNodeTest {

    /**
     * Tests that getPath() correctly constructs a path for a node and its parent
     * when both nodes have null names. The path should be "parentName/childName".
     */
    @Test
    public void shouldReturnPathForNodeWithParentWhenNodeNamesAreNull() {
        // Arrange
        // 1. Create a parent and child node structure.
        //    IIOMetadataNode is used as a concrete implementation of org.w3c.dom.Node.
        //    By default, its node name is null.
        Node parentNode = new IIOMetadataNode();
        Node childNode = new IIOMetadataNode();
        parentNode.appendChild(childNode);

        // 2. Create a dummy XPathParser required by the XNode constructor.
        //    Its internal state is not relevant for this specific test.
        Properties variables = new Properties();
        XPathParser parser = new XPathParser(null, false, variables, null);

        // 3. Create the XNode instance to be tested, wrapping the child node.
        XNode xNode = new XNode(parser, childNode, variables);

        // Act
        String actualPath = xNode.getPath();

        // Assert
        // The path is constructed as "parentName/childName". Since both nodes
        // were created without names, getNodeName() returns null for both,
        // resulting in the string "null/null".
        assertEquals("null/null", actualPath);
    }
}