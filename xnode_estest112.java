package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getEnumAttribute() returns null when the requested attribute does not exist on the XML node.
     */
    @Test
    public void getEnumAttributeShouldReturnNullWhenAttributeIsMissing() {
        // Arrange: Create an XNode from a DOM node that has no attributes.
        Node domNode = new IIOMetadataNode("test-node");
        XNode xNode = new XNode(null, domNode, new Properties());
        String attributeName = "countryCode"; // An attribute that does not exist on the node.

        // Act: Attempt to get an enum value from the non-existent attribute.
        Locale.IsoCountryCode result = xNode.getEnumAttribute(Locale.IsoCountryCode.class, attributeName);

        // Assert: The method should return null, indicating the attribute was not found.
        assertNull("Expected null for a missing enum attribute", result);
    }
}