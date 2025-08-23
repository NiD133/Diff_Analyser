package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class, focusing on its construction logic.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that when a {@link JsonTypeInfo.Value} is constructed with a null
     * property name, it correctly falls back to the default property name
     * associated with the given {@link JsonTypeInfo.Id}.
     */
    @Test
    public void shouldDefaultToIdPropertyNameWhenConstructedWithNullName() {
        // Arrange
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.NAME;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.PROPERTY;
        Class<Integer> defaultImpl = Integer.class;
        boolean idVisible = false;
        Boolean requireTypeIdForSubtypes = Boolean.FALSE;

        // The propertyName is explicitly null to test the fallback mechanism.
        String propertyName = null;

        // Act
        JsonTypeInfo.Value result = JsonTypeInfo.Value.construct(
                idType,
                inclusion,
                propertyName,
                defaultImpl,
                idVisible,
                requireTypeIdForSubtypes
        );

        // Assert
        // For Id.NAME, the default property name is "@type".
        assertEquals(idType.getDefaultPropertyName(), result.getPropertyName());
        assertFalse(result.getIdVisible());
    }
}