package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Tests that getStringAttribute() returns the default value from a supplier
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void shouldReturnDefaultValueFromSupplierWhenAttributeIsMissing() {
        // Arrange
        // 1. Create an empty XML node that has no attributes.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());

        // 2. Define the attribute we are looking for and its expected default value.
        String attributeName = "id";
        String defaultValue = "default-id-123";

        // 3. Mock a supplier to provide the default value when called.
        @SuppressWarnings("unchecked") // Mockito's mock() returns a raw Supplier
        Supplier<String> defaultValueSupplier = mock(Supplier.class);
        when(defaultValueSupplier.get()).thenReturn(defaultValue);

        // Act
        // Attempt to get the non-existent attribute, which should trigger the supplier.
        String actualValue = xNode.getStringAttribute(attributeName, defaultValueSupplier);

        // Assert
        // 1. The returned value should be the default value provided by the supplier.
        assertEquals(defaultValue, actualValue);

        // 2. Verify that the supplier's get() method was called exactly once to confirm
        //    that the default value was sourced correctly.
        verify(defaultValueSupplier).get();
    }
}