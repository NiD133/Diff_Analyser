package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getIntAttribute() returns the provided default value
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void getIntAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // 1. Create a DOM node that has no attributes.
        //    An XPathParser is not needed for attribute parsing, so it can be null.
        Node nodeWithoutAttributes = new IIOMetadataNode("test-node");
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        // 2. Define a non-existent attribute name and a default value.
        String nonExistentAttributeName = "port";
        Integer defaultValue = 8080;

        // Act
        // Attempt to get an integer attribute that does not exist, providing the default.
        Integer actualValue = xNode.getIntAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        // The method should return the default value.
        assertEquals(defaultValue, actualValue);
    }
}