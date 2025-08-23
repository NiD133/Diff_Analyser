package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Tests that getDoubleBody() returns the provided default value (null in this case)
     * when the XML node has no body content.
     */
    @Test
    public void getDoubleBodyShouldReturnNullDefaultWhenNodeBodyIsEmpty() {
        // Arrange: Create an XNode from an empty XML node, which results in a null body.
        Node emptyXmlNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyXmlNode, new Properties());
        Double defaultValue = null;

        // Act: Call the method under test with a null default value.
        Double result = xNode.getDoubleBody(defaultValue);

        // Assert: The method should return the provided default value because the node's body is empty.
        assertNull("Expected the default value (null) when the node body is empty", result);
    }
}