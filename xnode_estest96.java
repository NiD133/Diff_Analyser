package org.apache.ibatis.parsing;

import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * Test suite for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getLongAttribute throws a NumberFormatException
     * when the attribute's value is not a valid long.
     */
    @Test(expected = NumberFormatException.class)
    public void getLongAttributeShouldThrowNumberFormatExceptionForNonNumericValue() {
        // Arrange
        final String attributeName = "id";
        final String nonNumericValue = "this-is-not-a-number";

        // Create a DOM node with an attribute that has a non-numeric value.
        Node node = new IIOMetadataNode();
        // IIOMetadataNode implements Element, so we can use setAttribute.
        ((IIOMetadataNode) node).setAttribute(attributeName, nonNumericValue);

        // Create dummy dependencies required by the XNode constructor.
        XPathParser dummyParser = new XPathParser((Document) null);
        Properties variables = new Properties();

        XNode xNode = new XNode(dummyParser, node, variables);

        // Act
        // Attempt to parse the non-numeric attribute as a long.
        // This call is expected to throw a NumberFormatException.
        xNode.getLongAttribute(attributeName);

        // Assert
        // The @Test(expected) annotation handles the exception assertion.
    }
}