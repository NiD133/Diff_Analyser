package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that calling evalDouble with a syntactically invalid XPath expression
     * throws a RuntimeException.
     */
    @Test
    public void evalDoubleShouldThrowExceptionForInvalidXPathExpression() {
        // Arrange
        // 1. Define an XPath expression that is syntactically incorrect.
        String invalidXPath = "H%7;)SgDF9";

        // 2. Set up a minimal XNode instance. The underlying document can be null
        //    because the XPath parsing fails before the document is ever accessed.
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Node domNode = new IIOMetadataNode(); // A concrete, empty Node implementation
        XNode xNode = new XNode(xPathParser, domNode, new Properties());

        // Act & Assert
        try {
            xNode.evalDouble(invalidXPath);
            fail("Expected a RuntimeException to be thrown for the invalid XPath expression.");
        } catch (RuntimeException e) {
            // The exception is expected. We can inspect its message for more specific verification.
            String actualMessage = e.getMessage();
            assertTrue("Exception message should indicate an XPath evaluation error. Actual: " + actualMessage,
                    actualMessage.contains("Error evaluating XPath"));
            assertTrue("Exception message should detail the specific parsing error. Actual: " + actualMessage,
                    actualMessage.contains("Extra illegal tokens"));
        }
    }
}