package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * This test class contains tests for the {@link XNode} class.
 * The original class name and inheritance are preserved to maintain compatibility
 * with the existing test suite structure.
 */
public class XNode_ESTestTest32 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getDoubleBody(defaultValue) returns the provided default value
     * when the XNode is based on a DOM node with an empty body.
     */
    @Test
    public void getDoubleBodyShouldReturnDefaultValueWhenBodyIsEmpty() {
        // Arrange: Create an XNode that wraps a DOM Node with no text content.
        // An IIOMetadataNode is a standard, concrete implementation of org.w3c.dom.Node
        // and is suitable for creating a simple, empty node for this test.
        Node emptyNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, emptyNode, variables);

        Double defaultValue = 97.0;

        // Act: Call the method under test with the default value.
        Double actualValue = xNode.getDoubleBody(defaultValue);

        // Assert: The result should be the same as the default value provided.
        assertEquals("Expected the default value for an empty node body", defaultValue, actualValue);
    }
}