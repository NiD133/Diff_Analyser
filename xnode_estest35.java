package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Unit tests for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getDoubleAttribute() returns the provided default value
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void getDoubleAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // 1. Create a DOM node that has a name but no attributes.
        //    IIOMetadataNode is a standard implementation of org.w3c.dom.Node.
        Node nodeWithoutAttributes = new IIOMetadataNode("root");

        // 2. Instantiate XNode. The XPathParser and variables are required for the
        //    constructor but are not central to this test's logic.
        XPathParser parser = new XPathParser((Document) null, true);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, nodeWithoutAttributes, variables);

        // 3. Define a non-existent attribute name and a default value.
        String nonExistentAttributeName = "age";
        Double defaultValue = 30.0;

        // Act
        // Attempt to get the double value of an attribute that isn't there.
        Double actualValue = xNode.getDoubleAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        // The method should return the default value.
        assertEquals(defaultValue, actualValue, 0.0);
    }
}