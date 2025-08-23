package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * This test verifies that getFloatBody(defaultValue) returns the provided
     * default value when the XNode's body is null or empty.
     */
    @Test
    public void getFloatBodyShouldReturnDefaultValueWhenBodyIsNull() {
        // Arrange
        // An XPathParser is required by the XNode constructor, but its internal state is not
        // relevant for this test. We can pass a null document as the original test did.
        XPathParser parser = new XPathParser((Document) null, false);

        // Create a DOM node that results in a null body within the XNode.
        // An IIOMetadataNode with no children serves this purpose perfectly.
        Node emptyNode = new IIOMetadataNode();
        
        // The XNode constructor also requires a Properties object for variables.
        Properties variables = new Properties();

        XNode xnode = new XNode(parser, emptyNode, variables);

        Float defaultValue = 293.0f;

        // Act
        // Call the method under test. Since the node's body is null,
        // we expect the default value to be returned.
        Float actualValue = xnode.getFloatBody(defaultValue);

        // Assert
        // Verify that the returned value is indeed the default value.
        assertEquals(defaultValue, actualValue);
    }
}