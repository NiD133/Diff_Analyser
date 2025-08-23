package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XNode} class, focusing on body parsing methods.
 */
public class XNodeTest {

    /**
     * Verifies that getDoubleBody() returns the provided default value
     * when the node's body is empty.
     */
    @Test
    public void shouldReturnDefaultDoubleWhenBodyIsEmpty() {
        // Arrange
        // An IIOMetadataNode is a concrete org.w3c.dom.Node implementation.
        // Creating a new instance results in a node with a null/empty body.
        IIOMetadataNode emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Double defaultValue = 0.0;

        // Act
        Double actualValue = xNode.getDoubleBody(defaultValue);

        // Assert
        assertEquals(defaultValue, actualValue);
    }
}