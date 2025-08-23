package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;
import java.util.Properties;

/**
 * Contains tests for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling getIntAttribute with a null attribute name
     * throws a NullPointerException.
     *
     * This is the expected behavior because the underlying Properties object,
     * which stores the node's attributes, does not permit null keys.
     */
    @Test(expected = NullPointerException.class)
    public void getIntAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Create an XNode instance with a basic, empty DOM Node.
        // An IIOMetadataNode is a concrete implementation of org.w3c.dom.Node.
        Node emptyNode = new IIOMetadataNode();
        Properties emptyVariables = new Properties();

        // The XPathParser is not relevant for this specific method call, so it can be null.
        XNode xNode = new XNode(null, emptyNode, emptyVariables);

        // Act: Attempt to retrieve an integer attribute using a null name.
        // This is expected to trigger the exception.
        xNode.getIntAttribute(null);

        // Assert: The @Test(expected) annotation asserts that a
        // NullPointerException is thrown.
    }
}