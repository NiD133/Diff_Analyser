package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XNode class, focusing on attribute parsing.
 */
public class XNodeAttributeTest {

    /**
     * Verifies that getLongAttribute() returns the provided default value
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void getLongAttributeShouldReturnDefaultValueForMissingAttribute() {
        // Arrange
        // 1. Define a default value and the name of an attribute that we know does not exist.
        final Long expectedDefaultValue = -2217L;
        final String nonExistentAttributeName = "port";

        // 2. Create an empty DOM Node that has no attributes.
        // IIOMetadataNode is a standard, concrete implementation of org.w3c.dom.Node.
        Node emptyXmlNode = new IIOMetadataNode();

        // 3. Create the XNode instance to be tested. The XPathParser is not used by
        // the getLongAttribute method, so it can be null for this specific test.
        XNode xNode = new XNode(null, emptyXmlNode, new Properties());

        // Act
        // Attempt to retrieve the long value of the non-existent attribute.
        Long actualValue = xNode.getLongAttribute(nonExistentAttributeName, expectedDefaultValue);

        // Assert
        // The method should fall back to the provided default value.
        assertEquals(expectedDefaultValue, actualValue);
    }
}