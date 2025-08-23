package com.fasterxml.jackson.annotation;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * This test verifies that creating a {@link JsonIncludeProperties.Value}
     * from an annotation with an empty `value` array results in a `Value`
     * instance that represents an empty set of included properties.
     */
    @Test
    public void fromAnnotationWithNoPropertiesShouldYieldEmptyValue() {
        // Arrange: Create a mock annotation that is configured to have no included properties.
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class);
        when(mockAnnotation.value()).thenReturn(new String[0]);

        // Act: Create a Value instance from the mock annotation.
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(mockAnnotation);

        // Assert: The resulting Value should contain an empty, non-null set of included properties.
        assertNotNull("The created Value instance should not be null.", value);
        
        Set<String> includedProperties = value.getIncluded();
        assertNotNull("The set of included properties should not be null.", includedProperties);
        assertTrue("The set of included properties should be empty.", includedProperties.isEmpty());
    }
}