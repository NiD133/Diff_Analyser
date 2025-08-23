package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;

// Note: The original test class name and scaffolding are preserved as per the prompt's context.
// In a real-world scenario, these would likely be renamed (e.g., to XNodeTest) and simplified.
public class XNode_ESTestTest26 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getFloatBody() returns the provided default value
     * when the XML node's body is empty or null.
     */
    @Test
    public void getFloatBodyShouldReturnDefaultValueWhenBodyIsEmpty() {
        // Arrange
        // Create an XNode with an empty body. An IIOMetadataNode is used here as a
        // concrete, empty implementation of a W3C Node, which results in a null body.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        
        Float defaultValue = -3170.6738F;

        // Act
        Float actualValue = xNode.getFloatBody(defaultValue);

        // Assert
        // Since the node's body is empty, the method should return the default value.
        assertEquals(defaultValue, actualValue);
    }
}