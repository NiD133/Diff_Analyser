package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * Test suite for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getFloatAttribute throws a NumberFormatException
     * when the attribute's value cannot be parsed as a float.
     */
    @Test(expected = NumberFormatException.class)
    public void getFloatAttributeShouldThrowNumberFormatExceptionForInvalidValue() {
        // Arrange: Create a node with an attribute whose value is not a valid float.
        IIOMetadataNode node = new IIOMetadataNode("element");
        String attributeName = "value";
        String invalidFloatValue = "not-a-float";
        node.setAttribute(attributeName, invalidFloatValue);

        // The XPathParser is a required constructor argument but is not used by the
        // getFloatAttribute method, so it can be null for this test.
        XNode xNode = new XNode(null, node, new Properties());

        Float defaultValue = 99.9f;

        // Act & Assert: Attempt to get the attribute as a float.
        // This call is expected to throw a NumberFormatException.
        // The @Test(expected=...) annotation handles the assertion.
        xNode.getFloatAttribute(attributeName, defaultValue);
    }
}