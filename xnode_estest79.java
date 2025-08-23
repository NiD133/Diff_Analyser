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
     * Tests that getEnumAttribute returns the provided default value (null in this case)
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void getEnumAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // 1. Create an XNode that wraps an empty DOM node, which has no attributes.
        // We use IIOMetadataNode as a simple, concrete implementation of org.w3c.dom.Node.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());

        // 2. Define the attribute we are looking for and the default value to use if it's not found.
        String nonExistentAttributeName = "resultType";
        Locale.IsoCountryCode defaultValue = null;

        // Act
        // 3. Attempt to retrieve the enum attribute.
        Locale.IsoCountryCode result = xNode.getEnumAttribute(Locale.IsoCountryCode.class, nonExistentAttributeName, defaultValue);

        // Assert
        // 4. Verify that the method returned the default value, as the attribute was not present.
        assertNull("Expected the default value (null) to be returned for a missing attribute.", result);
    }
}