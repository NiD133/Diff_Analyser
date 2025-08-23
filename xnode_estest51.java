package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getIntAttribute throws a NullPointerException
     * when the attribute name provided is null.
     */
    @Test(expected = NullPointerException.class)
    public void getIntAttributeWithNullNameShouldThrowException() {
        // Arrange: Create an XNode with minimal valid dependencies.
        // The XPathParser is not used by getIntAttribute, so it can be null.
        Node domNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, domNode, variables);
        Integer anyDefaultValue = 100;

        // Act: Call the method under test with a null attribute name.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        xNode.getIntAttribute(null, anyDefaultValue);
    }
}