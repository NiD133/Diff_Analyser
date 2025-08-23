package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class, focusing on attribute parsing behavior.
 */
public class XNode_ESTestTest53 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getFloatAttribute throws a NullPointerException when the attribute name is null.
     * This is the expected behavior because the underlying Properties map, which stores
     * the attributes, does not permit null keys.
     */
    @Test(expected = NullPointerException.class)
    public void getFloatAttributeShouldThrowNPEForNullAttributeName() {
        // Arrange: Create an XNode instance.
        // The XPathParser is not needed for this test, so it can be null.
        // The node itself can be a simple, empty node.
        Node domNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, domNode, new Properties());
        Float defaultValue = 0.0F;

        // Act & Assert: Attempting to get an attribute with a null name should throw.
        xNode.getFloatAttribute(null, defaultValue);
    }
}