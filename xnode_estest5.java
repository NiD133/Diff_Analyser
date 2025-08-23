package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getStringBody(defaultValue) returns the provided default value
     * when the underlying XML node has no text content (i.e., a null body).
     */
    @Test
    public void getStringBodyWithDefaultShouldReturnDefaultWhenBodyIsNull() {
        // Arrange: Create an XNode from an empty DOM Node, which will have a null body.
        // We use IIOMetadataNode as a convenient, standard JDK implementation of org.w3c.dom.Node.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        String expectedDefault = "default-body-text";

        // Act: Call the method under test.
        String actualBody = xNode.getStringBody(expectedDefault);

        // Assert: The returned value should be the default string.
        assertEquals("Should return the default value for a node with no body content", expectedDefault, actualBody);
    }
}