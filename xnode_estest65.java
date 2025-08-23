package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.xpath.XPathExpressionException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling evalBoolean with a syntactically invalid XPath expression
     * throws a BuilderException.
     */
    @Test
    public void evalBooleanShouldThrowBuilderExceptionForInvalidXPathExpression() {
        // Arrange
        // An XPathParser is required by the XNode constructor. A null document is sufficient for this test.
        XPathParser xpathParser = new XPathParser(null, false, null);
        Node node = new IIOMetadataNode(); // A simple, empty DOM node.
        Properties variables = new Properties();
        XNode xNode = new XNode(xpathParser, node, variables);

        // An XPath expression that is syntactically invalid.
        String invalidXPathExpression = "=";

        // Act & Assert
        try {
            xNode.evalBoolean(invalidXPathExpression);
            fail("Should have thrown a BuilderException due to the invalid XPath expression.");
        } catch (BuilderException e) {
            // The exception message should clearly indicate an XPath evaluation error.
            assertTrue("Exception message should indicate an XPath error",
                e.getMessage().startsWith("Error evaluating XPath."));
            // The cause should be the original exception from the underlying XPath engine.
            assertTrue("Exception cause should be XPathExpressionException",
                e.getCause() instanceof XPathExpressionException);
        }
    }
}