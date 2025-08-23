package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class, focusing on attribute handling.
 */
public class XNodeTest {

    /**
     * Verifies that getStringAttribute() returns the provided default value
     * when the requested attribute does not exist on the underlying XML node.
     */
    @Test
    public void getStringAttributeShouldReturnDefaultValueForMissingAttribute() {
        // Arrange
        // Create a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();

        // The XNode constructor requires an XPathParser and variables, even if they are not
        // directly used for this specific test of reading a non-existent attribute.
        XPathParser dummyParser = new XPathParser((Document) null, true);
        XNode xNode = new XNode(dummyParser, nodeWithoutAttributes, new Properties());

        String attributeName = "nonExistentAttribute";
        String expectedDefaultValue = "defaultValue";

        // Act
        String actualValue = xNode.getStringAttribute(attributeName, expectedDefaultValue);

        // Assert
        assertEquals("Should return the default value when the attribute is not found.",
                expectedDefaultValue, actualValue);
    }
}