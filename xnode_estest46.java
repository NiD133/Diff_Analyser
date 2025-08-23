package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;
import java.util.Properties;

/**
 * Test suite for the {@link XNode} class.
 * This refactored test focuses on a specific failure case.
 */
// The original test class name and inheritance are preserved as their full context is not provided.
// In a real-world scenario, the class would likely be renamed to XNodeTest.
public class XNode_ESTestTest46 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that {@link XNode#getStringAttribute(String, String)} throws a
     * NullPointerException when the attribute name provided is null.
     *
     * This is expected behavior, as the underlying implementation likely uses a
     * collection (like java.util.Properties) that disallows null keys.
     */
    @Test(expected = NullPointerException.class)
    public void getStringAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Set up the test objects.
        // We need a dummy Node and an empty Properties object to construct an XNode.
        Node dummyNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, dummyNode, variables);
        String defaultValue = "default-value";

        // Act & Assert: Call the method with a null attribute name.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        xNode.getStringAttribute(null, defaultValue);
    }
}