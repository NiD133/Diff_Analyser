package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;

/**
 * This test class contains tests for the XNode class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class XNode_ESTestTest61 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that calling evalString() on an XNode initialized with a null XPathParser
     * results in a NullPointerException. This is the expected behavior, as the evaluation
     * methods rely on the XPathParser instance.
     */
    @Test(expected = NullPointerException.class)
    public void evalStringShouldThrowNullPointerExceptionWhenXPathParserIsNull() {
        // Arrange: Create an XNode with a null XPathParser.
        // The specific node type and variables are not critical for this test's objective.
        Node domNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, domNode, variables);

        // Act & Assert: Attempting to evaluate an expression should throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion, failing the test
        // if this specific exception is not thrown.
        xNode.evalString("any/xpath/expression");
    }
}