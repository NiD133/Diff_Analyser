package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that XNode.getName() returns null if the underlying DOM Node's name is null.
     */
    @Test
    public void getNameShouldReturnNullWhenWrappedNodeNameIsNull() {
        // Arrange
        // An IIOMetadataNode is used here because its default constructor creates a
        // DOM Node implementation for which getNodeName() returns null. This allows us
        // to test how XNode handles such a case.
        Node nodeWithNullName = new IIOMetadataNode();

        // Create minimal dependencies required by the XNode constructor.
        XPathParser dummyParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(dummyParser, nodeWithNullName, variables);

        // Act
        String name = xNode.getName();

        // Assert
        assertNull("The name should be null when the underlying node's name is null.", name);
    }
}