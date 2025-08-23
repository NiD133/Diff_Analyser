package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getFloatAttribute() returns the provided default value
     * when the requested attribute does not exist on the node.
     */
    @Test
    public void getFloatAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // Create an XNode from a DOM node that has no attributes.
        // The XPathParser is null because it is not used by the getFloatAttribute method.
        IIOMetadataNode emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());

        final String nonExistentAttributeName = "missingAttribute";
        final Float defaultValue = 123.45f;

        // Act
        Float actualValue = xNode.getFloatAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        // The returned value should be the exact default value provided.
        // A delta of 0.0f is used for an exact comparison.
        assertEquals(defaultValue, actualValue, 0.0f);
    }
}