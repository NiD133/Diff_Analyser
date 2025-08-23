package org.apache.ibatis.parsing;

import org.junit.Test;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getFloatAttribute throws a NumberFormatException
     * when the attribute value is not a valid float.
     */
    @Test(expected = NumberFormatException.class)
    public void getFloatAttributeShouldThrowExceptionForNonNumericValue() {
        // Arrange
        // Create a DOM Node with an attribute whose value is not a number.
        Node node = new IIOMetadataNode("node");
        String attributeName = "value";
        String nonNumericValue = "not-a-float";
        node.setAttribute(attributeName, nonNumericValue);

        // The XPathParser and Properties are required by the XNode constructor
        // but are not relevant to this specific test.
        XPathParser parser = new XPathParser(null, false, null, null);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, node, variables);

        // Act
        // Attempt to parse the non-numeric attribute as a float.
        xNode.getFloatAttribute(attributeName);

        // Assert
        // The @Test(expected) annotation asserts that a NumberFormatException is thrown.
    }
}