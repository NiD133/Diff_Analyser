package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Tests that getDoubleAttribute() returns the provided default value
     * when the specified attribute does not exist on the XML node.
     */
    @Test
    public void getDoubleAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // 1. Create a simple XML node without any attributes.
        //    IIOMetadataNode is a concrete implementation of org.w3c.dom.Node,
        //    suitable for creating a test node without a full XML document.
        Node nodeWithoutAttributes = new IIOMetadataNode("configuration");

        // 2. The XNode constructor requires an XPathParser. For this test, its
        //    internal state is not important, so we can initialize it with a null document.
        XPathParser xPathParser = new XPathParser((Document) null, true);
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, nodeWithoutAttributes, variables);

        // 3. Define the name of the non-existent attribute and the expected default value.
        String attributeName = "port";
        Double defaultValue = 768.82985;

        // Act
        // Attempt to retrieve the double value of an attribute that does not exist.
        Double actualValue = xNode.getDoubleAttribute(attributeName, defaultValue);

        // Assert
        // The returned value should be the default value we provided.
        // A delta of 0.0 is used for the floating-point comparison, requiring an exact match.
        assertEquals(defaultValue, actualValue, 0.0);
    }
}