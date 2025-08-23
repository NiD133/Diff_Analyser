package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getLongAttribute() returns null when the requested attribute
     * does not exist and no default value is provided.
     */
    @Test
    public void getLongAttributeShouldReturnNullWhenAttributeIsMissing() {
        // Arrange
        // Create a simple DOM node that has no attributes.
        // IIOMetadataNode is a standard, concrete implementation of org.w3c.dom.Node.
        Node nodeWithoutAttributes = new IIOMetadataNode();

        // Instantiate an XNode. The XPathParser and Properties are required dependencies,
        // but are not directly used in this test's logic.
        XPathParser parser = new XPathParser((Document) null, true);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, nodeWithoutAttributes, variables);

        // Act
        // Attempt to retrieve a 'long' attribute that is not present on the node.
        Long attributeValue = xNode.getLongAttribute("nonExistentAttribute");

        // Assert
        // The result should be null, as the attribute was not found.
        assertNull(attributeValue);
    }
}