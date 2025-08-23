package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies that the valueFor() method correctly returns the
     * {@link JsonIncludeProperties} annotation class it is associated with.
     * This behavior is part of the contract for the {@link JacksonAnnotationValue} interface.
     */
    @Test
    public void valueFor_shouldReturnAssociatedAnnotationClass() {
        // Arrange
        // The .ALL instance is a default, pre-configured value representing the inclusion of all properties.
        JsonIncludeProperties.Value allPropertiesValue = JsonIncludeProperties.Value.ALL;

        // Act
        // The valueFor() method should return the annotation type this value class corresponds to.
        Class<JsonIncludeProperties> annotationClass = allPropertiesValue.valueFor();

        // Assert
        // The returned class should be exactly JsonIncludeProperties.class.
        // Comparing class literals is more robust than comparing string representations.
        assertEquals(JsonIncludeProperties.class, annotationClass);
    }
}