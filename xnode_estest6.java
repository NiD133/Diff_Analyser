package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import java.util.function.Supplier;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getStringAttribute() returns the value from the default supplier
     * when the requested attribute does not exist on the XML node.
     * This specific case checks the behavior when the supplier provides a null value.
     */
    @Test
    public void getStringAttributeWithSupplierShouldReturnSuppliedValueWhenAttributeIsMissing() {
        // Arrange
        // 1. Create a simple XML node with no attributes.
        //    IIOMetadataNode is a convenient, concrete implementation of org.w3c.dom.Node.
        Node nodeWithNoAttributes = new IIOMetadataNode();
        XNode xnode = new XNode(null, nodeWithNoAttributes, new Properties());

        // 2. Create a mock supplier that will return null as the default value.
        @SuppressWarnings("unchecked")
        Supplier<String> defaultValueSupplier = mock(Supplier.class);
        when(defaultValueSupplier.get()).thenReturn(null);

        // Act
        // Attempt to get a non-existent attribute, providing the supplier for a default value.
        String result = xnode.getStringAttribute("missingAttribute", defaultValueSupplier);

        // Assert
        // The result should be null, as that's what the default supplier returned.
        assertNull(result);
    }
}