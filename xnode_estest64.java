package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * This test class is a refactored version of an auto-generated test.
 * The original class name was XNode_ESTestTest64.
 */
public class XNode_ESTestTest64 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that calling evalDouble() on an XNode instance created with a null
     * XPathParser throws a NullPointerException. This ensures the method is robust
     * against invalid initial states.
     */
    @Test(expected = NullPointerException.class)
    public void evalDoubleShouldThrowNullPointerExceptionWhenXPathParserIsNull() {
        // Arrange: Create an XNode with a null XPathParser.
        // The XPathParser is intentionally null to trigger the expected exception.
        Properties variables = new Properties();
        Node dummyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, dummyNode, variables);

        // Act & Assert: Attempting to evaluate an XPath expression should fail.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        xNode.evalDouble("/any/xpath/expression");
    }
}