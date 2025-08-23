package org.apache.ibatis.parsing;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that evalNodes returns an empty list when the XPath expression
     * does not match any nodes within the current context.
     */
    @Test
    public void evalNodesShouldReturnEmptyListForNonexistentPath() {
        // Arrange
        // An empty DOM node serves as the context for our XNode.
        Node emptyNode = new IIOMetadataNode();
        XPathParser parser = new XPathParser((Document) null, true);
        XNode xNode = new XNode(parser, emptyNode, new Properties());

        // Act
        // Attempt to find nodes using an XPath expression that will not match anything.
        List<XNode> resultNodes = xNode.evalNodes("nonexistent/path");

        // Assert
        // The method should return an empty list, confirming no nodes were found.
        assertTrue("Evaluating a non-existent path should result in an empty list.", resultNodes.isEmpty());
    }
}