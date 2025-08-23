package org.apache.ibatis.parsing;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This test verifies the error-handling behavior of the XNode class.
 * It ensures that evaluating an invalid XPath expression results in a descriptive RuntimeException.
 */
public class XNodeTest {

    @Test
    public void evalNodeWithInvalidXPathShouldThrowRuntimeException() {
        // Arrange
        // An invalid XPath expression that cannot be parsed or evaluated as a number.
        final String invalidXPathExpression = "2iY";

        // Create minimal dependencies to instantiate an XNode.
        Node dummyNode = new IIOMetadataNode();
        Properties variables = new Properties();

        // The XPathParser is initialized with a null document. This is sufficient for this test
        // as the error occurs during expression evaluation, not node traversal in a document.
        XPathParser parser = new XPathParser((Document) null, false);
        XNode xNode = new XNode(parser, dummyNode, variables);

        // Act & Assert
        try {
            xNode.evalNode(invalidXPathExpression);
            fail("Expected a RuntimeException for an invalid XPath expression, but none was thrown.");
        } catch (RuntimeException e) {
            // The exception is expected. We verify its message to ensure it's the correct error.
            String actualMessage = e.getMessage();
            String expectedRootCauseText = "could not be formatted to a number";

            assertTrue("Exception message should indicate an XPath evaluation error. Actual: " + actualMessage,
                    actualMessage.contains("Error evaluating XPath"));
            assertTrue("Exception message should explain the root cause of the failure. Actual: " + actualMessage,
                    actualMessage.contains(expectedRootCauseText));
        }
    }
}