package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link JsonTypeInfo.Value} class.
 * This test focuses on the construction of a {@code JsonTypeInfo.Value} instance
 * and the behavior of the {@code isEnabled} static helper method.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that {@link JsonTypeInfo.Value#isEnabled(JsonTypeInfo.Value)} returns true
     * for a configuration where polymorphic type handling is active (i.e., the type ID is not {@code NONE}).
     * This test also confirms that the constructor correctly initializes the object's properties.
     */
    @Test
    public void isEnabledShouldReturnTrueWhenConstructedWithActiveTypeId() {
        // Arrange: Define a configuration for polymorphic type handling.
        final JsonTypeInfo.Id idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        final JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        final String propertyName = "custom-type-property";
        final Class<?> defaultImpl = Object.class;
        final boolean idVisible = false;
        // The original test used Boolean.valueOf("some_random_string"), which evaluates to false.
        // We use 'false' directly for clarity.
        final Boolean requireTypeIdForSubtypes = false;

        // Act: Construct the JsonTypeInfo.Value and check if it's considered enabled.
        JsonTypeInfo.Value typeInfoValue = JsonTypeInfo.Value.construct(
                idType,
                inclusion,
                propertyName,
                defaultImpl,
                idVisible,
                requireTypeIdForSubtypes
        );
        boolean isEnabled = JsonTypeInfo.Value.isEnabled(typeInfoValue);

        // Assert: Verify that type handling is enabled and properties are set correctly.
        assertTrue("isEnabled() should be true because the IdType is not NONE.", isEnabled);
        assertEquals("Property name should be correctly set.",
                     propertyName, typeInfoValue.getPropertyName());
        assertFalse("idVisible flag should be correctly set.",
                    typeInfoValue.getIdVisible());
    }
}