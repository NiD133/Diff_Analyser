package org.apache.ibatis.parsing;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class, focusing on attribute parsing behavior.
 */
public class XNodeTest {

    /**
     * Verifies that getDoubleAttribute throws a NullPointerException
     * when the attribute name provided is null.
     */
    @Test(expected = NullPointerException.class)
    public void getDoubleAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Create an XNode with a basic DOM node and empty properties.
        // The XPathParser is not needed for this attribute-related test.
        Node domNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, domNode, variables);

        // Act: Call the method under test with a null argument.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        xNode.getDoubleAttribute(null);
    }
}