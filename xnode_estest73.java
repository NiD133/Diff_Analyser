package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link XNode} class.
 */
// The original test class name and inheritance are preserved.
public class XNode_ESTestTest73 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getLongAttribute() returns the provided default value (null in this case)
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void shouldReturnDefaultNullWhenLongAttributeIsMissing() {
        // Arrange: Create an XNode based on a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        String nonExistentAttribute = "someMissingAttribute";
        Long defaultValue = null;

        // Act: Attempt to retrieve the long value of the non-existent attribute.
        Long result = xNode.getLongAttribute(nonExistentAttribute, defaultValue);

        // Assert: The result should be the default value provided.
        assertNull("Expected the default value to be returned for a missing attribute.", result);
    }
}