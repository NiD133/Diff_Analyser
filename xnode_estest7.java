package org.apache.ibatis.parsing;

import org.junit.Test;
import java.util.Properties;
import java.util.function.Supplier;
import javax.imageio.metadata.IIOMetadataNode;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Tests {@link XNode#getStringAttribute(String, Supplier)}.
     *
     * This test verifies that when a requested attribute does not exist on the XML node,
     * the method correctly returns the default value provided by the given Supplier.
     */
    @Test
    public void shouldReturnDefaultValueFromSupplierWhenAttributeIsMissing() {
        // Arrange: Create an XNode from a DOM node that has no attributes.
        IIOMetadataNode nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        String expectedDefaultValue = "default-value";
        String attributeName = "nonExistentAttribute";

        // Arrange: Mock a supplier to provide the default value when called.
        @SuppressWarnings("unchecked") // Safe cast for a mock of a generic type
        Supplier<String> defaultValueSupplier = mock(Supplier.class);
        when(defaultValueSupplier.get()).thenReturn(expectedDefaultValue);

        // Act: Attempt to get the value of an attribute that does not exist.
        String actualValue = xNode.getStringAttribute(attributeName, defaultValueSupplier);

        // Assert: The returned value should be the one from the supplier.
        assertEquals(expectedDefaultValue, actualValue);

        // Assert: Verify that the supplier's get() method was called exactly once.
        verify(defaultValueSupplier, times(1)).get();
    }
}