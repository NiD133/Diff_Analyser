package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * This test class contains tests for the {@link XNode} class.
 * Note: The original auto-generated class name and hierarchy are preserved.
 */
public class XNode_ESTestTest59 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getBooleanAttribute() throws a NullPointerException
     * when the provided attribute name is null. This is the expected behavior
     * as the underlying Properties object does not accept null keys.
     */
    @Test(expected = NullPointerException.class)
    public void getBooleanAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Create a minimal XNode instance.
        // The XPathParser is not needed for this attribute-parsing test, so it can be null.
        // IIOMetadataNode is a concrete implementation of org.w3c.dom.Node used for testing.
        Node domNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, domNode, new Properties());

        // Act: Call the method under test with a null argument.
        xNode.getBooleanAttribute(null);

        // Assert: A NullPointerException is expected, as declared by the @Test annotation.
    }
}