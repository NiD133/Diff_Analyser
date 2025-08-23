package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link XNode}.
 * This specific test focuses on exception handling with invalid XPath expressions.
 */
public class XNode_ESTestTest113 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that XNode.evalNodes() throws a RuntimeException when provided with a
     * syntactically invalid XPath expression. The exception should be propagated
     * from the underlying XPathParser.
     */
    @Test(timeout = 4000)
    public void evalNodesShouldThrowRuntimeExceptionForInvalidXPathExpression() {
        // Arrange
        // This XPath expression is invalid because it contains an unclosed double quote literal.
        final String invalidXPathWithUnclosedQuote = "y6<OipM*RS!4=R\"";

        // To create an XNode instance, we need an XPathParser and a dummy Node.
        // The Document for the parser can be null because the syntax check happens
        // before any document traversal.
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Node dummyNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, dummyNode, variables);

        // Act & Assert
        try {
            xNode.evalNodes(invalidXPathWithUnclosedQuote);
            fail("Expected a RuntimeException to be thrown for the malformed XPath expression.");
        } catch (RuntimeException e) {
            // Verify that the exception is the expected one from the XPathParser.
            String errorMessage = e.getMessage();
            assertTrue("Exception message should indicate an XPath evaluation error.",
                    errorMessage.contains("Error evaluating XPath"));
            assertTrue("Exception message should detail the cause (e.g., 'misquoted literal').",
                    errorMessage.contains("misquoted literal"));
        }
    }
}