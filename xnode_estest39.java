package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Contains improved, understandable tests for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getBooleanBody() returns the provided default value
     * when the XNode is created from a node with no text content (i.e., a null body).
     */
    @Test
    public void getBooleanBodyShouldReturnDefaultValueWhenBodyIsNull() {
        // Arrange
        // An empty DOM node will result in the XNode having a null body.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Boolean defaultValue = false;

        // Act
        Boolean result = xNode.getBooleanBody(defaultValue);

        // Assert
        // The result should be the default value because the node's body is null.
        assertFalse("The result should be false as per the default value.", result);
        assertEquals(defaultValue, result);
    }
}