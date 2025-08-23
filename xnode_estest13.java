package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode} class, focusing on body parsing.
 */
public class XNodeTest {

    /**
     * Verifies that getLongBody(defaultValue) returns the default value
     * when the XNode's body is null (e.g., when wrapping an empty XML node).
     */
    @Test
    public void shouldReturnDefaultLongWhenBodyIsNull() {
        // Arrange
        // An empty DOM node will result in a null body for the XNode.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Long defaultValue = 11L;

        // Act
        // Attempt to get the body as a Long, providing a default value.
        Long actualValue = xNode.getLongBody(defaultValue);

        // Assert
        // The method should return the provided default value because the body is null.
        assertEquals(defaultValue, actualValue);
    }
}