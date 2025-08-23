package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getIntAttribute returns the default value when the attribute is not present.
     */
    @Test
    public void getIntAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // Create an empty XML node that has no attributes.
        IIOMetadataNode emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());

        String nonExistentAttribute = "id";
        Integer defaultValue = 100;

        // Act
        // Attempt to retrieve the integer value of an attribute that does not exist.
        Integer actualValue = xNode.getIntAttribute(nonExistentAttribute, defaultValue);

        // Assert
        // The method should return the provided default value.
        assertEquals(defaultValue, actualValue);
    }
}