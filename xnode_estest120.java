package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode} class.
 */
public class XNodeTest {

    @Test
    public void getDoubleAttributeShouldReturnNullWhenAttributeIsMissing() {
        // Arrange
        // Create a DOM Node that has no attributes. IIOMetadataNode is a convenient
        // concrete implementation of org.w3c.dom.Node for this purpose.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        XPathParser parser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, nodeWithoutAttributes, variables);

        // Act
        // Attempt to retrieve a non-existent attribute as a Double.
        Double attributeValue = xNode.getDoubleAttribute("nonExistentAttribute");

        // Assert
        // The result should be null, as the attribute is not defined on the node.
        assertNull(attributeValue);
    }
}