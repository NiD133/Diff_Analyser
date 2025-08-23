package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that calling {@link JsonTypeInfo.Value#withDefaultImpl(Class)} with the
     * same class that the object already has does not create a new instance,
     * but returns the original instance. This is an important optimization for
     * immutable value objects.
     */
    @Test
    public void withDefaultImpl_whenProvidedSameClass_shouldReturnSameInstance() {
        // Arrange
        final Class<?> defaultImplClass = Object.class;
        final String propertyName = "testProperty";

        // Construct the initial JsonTypeInfo.Value instance
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.construct(
                JsonTypeInfo.Id.MINIMAL_CLASS,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                propertyName,
                defaultImplClass,
                false, // idVisible
                false  // requireTypeIdForSubtypes
        );

        // Act
        // Call withDefaultImpl using the same class it was constructed with.
        JsonTypeInfo.Value resultValue = originalValue.withDefaultImpl(defaultImplClass);

        // Assert
        // The method should return the same instance, not a new one.
        assertSame("Expected the same instance when the default implementation class is unchanged",
                originalValue, resultValue);

        // Sanity checks to ensure other properties are unaffected.
        assertEquals(propertyName, resultValue.getPropertyName());
        assertFalse(resultValue.getIdVisible());
    }
}