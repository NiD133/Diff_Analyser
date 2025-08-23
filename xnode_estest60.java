package org.apache.ibatis.parsing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.xpath.XPathExpressionException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link XNode} class, focusing on its XPath evaluation capabilities.
 */
@DisplayName("XNode XPath Evaluation Tests")
class XNodeTest {

    @Test
    @DisplayName("evalString should throw RuntimeException for an invalid XPath expression")
    void evalStringShouldThrowRuntimeExceptionForInvalidXPathExpression() {
        // Arrange
        // An invalid XPath expression that looks like XML, which is a common user error.
        String invalidXPathExpression = "<null>\n  <null />\n</null>\n";

        // Create a minimal XNode instance to test the evalString method.
        // The actual node content does not matter here, as the XPath expression is syntactically invalid on its own.
        Node dummyNode = new IIOMetadataNode();
        XPathParser parser = new XPathParser((Document) null, false);
        XNode xNode = new XNode(parser, dummyNode, new Properties());

        // Act & Assert
        // We expect a RuntimeException because the underlying XPathParser will fail to parse the invalid expression.
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> xNode.evalString(invalidXPathExpression));

        // Verify the exception message and cause to ensure it's the correct error.
        assertTrue(thrownException.getMessage().contains("Error evaluating XPath"),
                "Exception message should indicate an XPath evaluation error.");
        assertTrue(thrownException.getCause() instanceof XPathExpressionException,
                "The root cause should be an XPathExpressionException.");
    }
}