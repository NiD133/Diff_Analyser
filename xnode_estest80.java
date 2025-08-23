package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.Properties;
import static org.junit.Assert.assertNull;

public class XNode_ESTestTest80 extends XNode_ESTest_scaffolding {

    /**
     * Tests that getFloatBody returns the provided default value when the node's body is empty.
     * An empty string cannot be parsed into a Float, so the method should fall back to the default.
     */
    @Test
    public void shouldReturnDefaultFloatWhenBodyIsEmpty() {
        // Arrange
        // Create a DOM Node that will result in an empty body in the XNode.
        // An IIOMetadataNode with no children is a simple way to achieve this.
        Node nodeWithEmptyBody = new IIOMetadataNode("node");
        
        // The XNode constructor requires an XPathParser and Properties, which can be dummies for this test.
        XPathParser dummyParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(dummyParser, nodeWithEmptyBody, variables);

        Float defaultValue = null;

        // Act
        Float actualValue = xNode.getFloatBody(defaultValue);

        // Assert
        assertNull("getFloatBody should return the default value for an unparsable (empty) body.", actualValue);
    }
}