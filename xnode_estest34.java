package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode#getDoubleBody(Double)} method.
 */
// The original test class name and inheritance are preserved.
public class XNode_ESTestTest34 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getDoubleBody(defaultValue) returns the provided default value
     * when the XNode is based on a node with no text content (i.e., a null body).
     */
    @Test
    public void getDoubleBodyShouldReturnDefaultValueWhenBodyIsNull() {
        // Arrange
        // An empty DOM node will result in the XNode having a null body.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Double defaultValue = -1696.0;

        // Act
        Double result = xNode.getDoubleBody(defaultValue);

        // Assert
        // The method should return the default value as the node's body is null.
        assertEquals(defaultValue, result, 0.0);
    }
}