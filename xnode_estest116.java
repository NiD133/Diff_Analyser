package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that evalString returns an empty string when the XPath expression
     * does not match any node or value. This is the expected behavior instead of
     * returning null.
     */
    @Test
    public void evalStringShouldReturnEmptyStringForNonexistentExpression() {
        // Arrange
        // Create an empty node to serve as the context for XPath evaluation.
        Node emptyNode = new IIOMetadataNode();

        // The XPathParser is initialized with a null document, ensuring that
        // the evaluation context is minimal.
        XPathParser xpathParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(xpathParser, emptyNode, variables);

        String nonExistentExpression = "some/non/existent/path";

        // Act
        // Attempt to evaluate an expression that will not be found in the empty node.
        String result = xNode.evalString(nonExistentExpression);

        // Assert
        // A non-matching expression should result in an empty string, not null.
        assertEquals("", result);
    }
}