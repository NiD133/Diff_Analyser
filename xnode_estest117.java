package org.apache.ibatis.parsing;

import static org.junit.Assert.assertTrue;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class, focusing on the getChildrenAsProperties method.
 */
public class XNodeTest {

    /**
     * Tests that getChildrenAsProperties returns an empty Properties object
     * when child nodes do not have the required 'name' and 'value' attributes.
     */
    @Test
    public void getChildrenAsPropertiesShouldReturnEmptyForChildWithoutNameAndValueAttributes() {
        // Arrange
        // Create a parent XML node with one child. The child node is intentionally
        // created without the 'name' and 'value' attributes.
        IIOMetadataNode parentNode = new IIOMetadataNode("properties");
        Node childNode = new IIOMetadataNode("property");
        parentNode.appendChild(childNode);

        // The XPathParser and variables are not used by the method under test in this scenario.
        XNode xNode = new XNode(null, parentNode, new Properties());

        // Act
        Properties resultProperties = xNode.getChildrenAsProperties();

        // Assert
        // The method should ignore the child node because it lacks the necessary attributes,
        // resulting in an empty Properties object.
        assertTrue("Properties should be empty when child nodes lack 'name' and 'value' attributes.",
                   resultProperties.isEmpty());
    }
}