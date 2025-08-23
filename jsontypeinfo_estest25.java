package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfo_ESTestTest25 { // Note: A better class name would be JsonTypeInfoValueTest

    /**
     * Verifies that the static `construct` method of `JsonTypeInfo.Value`
     * correctly creates an instance and initializes all its properties with the provided arguments.
     */
    @Test
    public void shouldConstructValueWithSpecifiedProperties() {
        // Arrange: Define the configuration for a JsonTypeInfo.Value instance.
        final JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        final JsonTypeInfo.As inclusionType = JsonTypeInfo.As.WRAPPER_OBJECT;
        final String propertyName = "custom-type-property";
        final Class<?> defaultImpl = JsonTypeInfo.class; // A marker for 'no default implementation'
        final boolean isIdVisible = true;
        
        // The original test used `new Boolean("")`, which evaluates to false.
        // Using Boolean.FALSE is much clearer and avoids unnecessary object creation.
        final Boolean requireTypeIdForSubtypes = Boolean.FALSE;

        // Act: Create the JsonTypeInfo.Value instance using the factory method.
        JsonTypeInfo.Value result = JsonTypeInfo.Value.construct(
                idType,
                inclusionType,
                propertyName,
                defaultImpl,
                isIdVisible,
                requireTypeIdForSubtypes
        );

        // Assert: Verify that all properties of the created instance match the arranged values.
        assertEquals("ID Type should be correctly set", idType, result.getIdType());
        assertEquals("Inclusion type should be correctly set", inclusionType, result.getInclusionType());
        assertEquals("Property name should be correctly set", propertyName, result.getPropertyName());
        assertEquals("Default implementation class should be correctly set", defaultImpl, result.getDefaultImpl());
        assertTrue("ID visibility should be true", result.getIdVisible());
        assertEquals("Requirement for type ID for subtypes should be false",
                requireTypeIdForSubtypes, result.getRequireTypeIdForSubtypes());
    }
}