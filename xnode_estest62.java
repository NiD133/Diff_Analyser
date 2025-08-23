package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling evalNodes() on an XNode initialized with a null XPathParser
     * results in a NullPointerException. This is expected behavior, as the XNode
     * delegates XPath evaluation to its XPathParser instance.
     */
    @Test
    public void evalNodesShouldThrowNullPointerExceptionWhenXPathParserIsNull() {
        // Arrange: Create an XNode with a null XPathParser.
        // The specific Node and Properties content are not relevant for this test case.
        Node domNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, domNode, variables);

        // Act & Assert: Verify that a NullPointerException is thrown when evalNodes is called.
        assertThrows(NullPointerException.class, () -> {
            xNode.evalNodes("/some/expression");
        });
    }
}