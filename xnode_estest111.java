package org.apache.ibatis.parsing;

import static org.junit.Assert.assertFalse;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode} class, focusing on XPath evaluation.
 */
public class XNodeTest {

    /**
     * Verifies that {@link XNode#evalBoolean(String)} returns false when the
     * XPath expression does not match any element in the current node context.
     */
    @Test
    public void evalBooleanShouldReturnFalseForNonexistentNode() {
        // Arrange
        // Create an empty DOM node to serve as the context for XPath evaluation.
        // IIOMetadataNode is a standard, concrete implementation of org.w3c.dom.Node.
        Node emptyNode = new IIOMetadataNode();
        
        // The XPathParser can be initialized with a null document for simple node evaluations.
        XPathParser parser = new XPathParser((Document) null, true);
        
        // The XNode requires a Properties object, but it's not used in this evaluation.
        Properties variables = new Properties();
        
        XNode xNode = new XNode(parser, emptyNode, variables);

        // Act
        // Evaluate an XPath expression ("name") that looks for a child element named "name".
        // Since emptyNode has no children, this expression will not find a match.
        Boolean result = xNode.evalBoolean("name");

        // Assert
        // A non-existent node-set in an XPath expression evaluates to false in a boolean context.
        assertFalse("evalBoolean should return false for a non-matching expression", result);
    }
}