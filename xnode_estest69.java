package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the XNode class.
 * This class contains the refactored test case.
 */
public class XNodeTest {

    /**
     * Verifies that getChildren() returns an empty list when the underlying XML node
     * has no child elements.
     */
    @Test
    public void shouldReturnEmptyListWhenNodeHasNoChildren() {
        // Arrange: Set up the test objects and preconditions.
        // 1. Create a DOM Node that has no children. IIOMetadataNode is a convenient
        //    standard library implementation of org.w3c.dom.Node.
        Node nodeWithoutChildren = new IIOMetadataNode();

        // 2. The XNode constructor requires an XPathParser and Properties.
        //    They are not used by the getChildren() method, so we can use simple instances.
        Properties variables = new Properties();
        XPathParser xPathParser = new XPathParser((Document) null, false, variables, null);

        // 3. Create the XNode instance to be tested.
        XNode xNode = new XNode(xPathParser, nodeWithoutChildren, variables);

        // Act: Call the method under test.
        List<XNode> children = xNode.getChildren();

        // Assert: Verify the outcome.
        assertTrue("Expected getChildren() to return an empty list for a node with no children.", children.isEmpty());
    }
}