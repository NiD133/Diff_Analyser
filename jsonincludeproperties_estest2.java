package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Tests that {@link JsonIncludeProperties.Value#from(JsonIncludeProperties)}
     * correctly creates a Value instance from a {@link JsonIncludeProperties} annotation,
     * populating the set of included properties.
     */
    @Test
    public void from_givenAnnotation_shouldCreateValueWithIncludedProperties() {
        // Arrange: Create a mock annotation with a specific set of property names.
        String[] propertyNames = {"id", "name"};
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class);
        when(mockAnnotation.value()).thenReturn(propertyNames);

        // Act: Call the factory method under test.
        JsonIncludeProperties.Value resultValue = JsonIncludeProperties.Value.from(mockAnnotation);

        // Assert: Verify that the resulting Value object is not null and contains the correct properties.
        assertNotNull("The created Value object should not be null.", resultValue);

        Set<String> expectedIncludedProperties = new HashSet<>(Arrays.asList(propertyNames));
        assertEquals("The set of included properties should match the annotation's value.",
                expectedIncludedProperties, resultValue.getIncluded());
    }
}