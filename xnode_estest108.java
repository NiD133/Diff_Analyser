package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getFloatAttribute throws a NullPointerException when the attribute name is null.
     */
    @Test(expected = NullPointerException.class)
    public void getFloatAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Create an XNode instance with a mock node and empty properties.
        // The XPathParser is not needed for this test, so it can be null.
        IIOMetadataNode dummyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, dummyNode, new Properties());

        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation will handle the assertion.
        xNode.getFloatAttribute(null);
    }
}