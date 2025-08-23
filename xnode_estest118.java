package org.apache.ibatis.parsing;

import static org.junit.Assert.assertSame;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that the getNode() method returns the exact same Node instance
     * that was passed to the XNode constructor.
     */
    @Test
    public void getNodeShouldReturnTheNodePassedToConstructor() {
        // Arrange
        Node originalNode = new IIOMetadataNode(); // A concrete implementation of Node for testing
        Properties variables = new Properties();
        // The XPathParser is not used by the constructor or getNode(), so it can be null for this test.
        XNode xNode = new XNode(null, originalNode, variables);

        // Act
        Node retrievedNode = xNode.getNode();

        // Assert
        assertSame("The retrieved node should be the same instance as the one used for construction.",
                originalNode, retrievedNode);
    }
}