package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getIntAttribute() returns null when the requested attribute does not exist on the node.
     */
    @Test
    public void getIntAttributeShouldReturnNullForNonExistentAttribute() {
        // Arrange: Create an XNode based on a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        Properties variables = new Properties();
        // The XPathParser is not used by the getIntAttribute method, so it can be null for this test.
        XNode xNode = new XNode(null, nodeWithoutAttributes, variables);
        String nonExistentAttributeName = "someAttribute";

        // Act: Attempt to retrieve the integer value of an attribute that isn't there.
        Integer attributeValue = xNode.getIntAttribute(nonExistentAttributeName);

        // Assert: The result should be null, indicating the attribute was not found.
        assertNull("Expected a null value for a non-existent integer attribute.", attributeValue);
    }
}