package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getLongBody(defaultValue) returns the provided default value
     * when the XNode's body is null.
     * <p>
     * An XNode's body is considered null if its underlying DOM Node is empty,
     * meaning it contains no text or CDATA content.
     */
    @Test
    public void getLongBodyShouldReturnDefaultValueWhenBodyIsNull() {
        // Arrange
        // Create an XNode from an empty DOM node, which results in a null body.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Long defaultValue = -1669L;

        // Act
        Long actualValue = xNode.getLongBody(defaultValue);

        // Assert
        assertEquals("getLongBody should return the default value for a null body",
                defaultValue, actualValue);
    }
}