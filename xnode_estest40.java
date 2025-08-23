package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getBooleanAttribute() returns the provided default value (null)
     * when the requested attribute does not exist on the underlying XML node.
     */
    @Test
    public void getBooleanAttributeShouldReturnNullDefaultWhenAttributeIsMissing() {
        // Arrange: Create an XNode from a DOM Node that has no attributes.
        // IIOMetadataNode is a convenient, standalone implementation of org.w3c.dom.Node.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        // The XPathParser is not used by the getBooleanAttribute method, so it can be null.
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        String nonExistentAttributeName = "someFlag";
        Boolean defaultValue = null;

        // Act: Attempt to retrieve the boolean attribute, providing a null default.
        Boolean actualValue = xNode.getBooleanAttribute(nonExistentAttributeName, defaultValue);

        // Assert: The method should return the default value since the attribute is not present.
        assertNull("Expected the default value (null) to be returned for a missing attribute.", actualValue);
    }
}