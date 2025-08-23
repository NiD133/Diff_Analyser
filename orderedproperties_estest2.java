package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test suite for {@link OrderedProperties}.
 * This class contains the test case from the original example.
 */
// The original class name "OrderedProperties_ESTestTest2" is kept as requested,
// but in a real-world scenario, it would be renamed to "OrderedPropertiesTest".
public class OrderedProperties_ESTestTest2 {

    /**
     * Tests that calling forEach on an empty OrderedProperties instance
     * does not invoke the consumer.
     */
    @Test
    public void forEachShouldDoNothingOnEmptyProperties() {
        // Arrange
        final OrderedProperties emptyProperties = new OrderedProperties();
        @SuppressWarnings("unchecked") // Necessary for mocking a generic type
        final BiConsumer<Object, Object> mockConsumer = mock(BiConsumer.class);

        // Act
        emptyProperties.forEach(mockConsumer);

        // Assert
        // Verify that the consumer's 'accept' method was never called.
        verify(mockConsumer, never()).accept(any(), any());
        
        // Also, confirm the properties object remains unchanged.
        assertTrue("The properties should remain empty after the forEach call", emptyProperties.isEmpty());
    }
}