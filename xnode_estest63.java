package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling an evaluation method (e.g., evalNode) on an XNode
     * instance created with a null XPathParser results in a NullPointerException.
     * This is an important contract check for the constructor.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenEvaluatingNodeWithNullParser() {
        // Arrange: Create an XNode with a null XPathParser.
        // This is the specific condition we want to test.
        Node dummyNode = new IIOMetadataNode();
        Properties emptyVariables = new Properties();
        XNode xNode = new XNode(null, dummyNode, emptyVariables);

        // Act & Assert: Attempt to evaluate an expression.
        // This action is expected to throw a NullPointerException because the internal
        // XPathParser, which is required for evaluation, is null.
        xNode.evalNode("/any/expression");
    }
}