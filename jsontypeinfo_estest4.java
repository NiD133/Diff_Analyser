package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonTypeInfo.Value} class,
 * specifically its immutability and "wither" methods.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that the {@code withRequireTypeIdForSubtypes} method creates a new,
     * distinct {@link JsonTypeInfo.Value} instance with the updated property,
     * while the original instance remains unchanged.
     */
    @Test
    public void withRequireTypeIdForSubtypes_shouldCreateNewUnequalInstance() {
        // Arrange: Start with the default EMPTY value, where 'requireTypeIdForSubtypes' is null.
        final JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.EMPTY;
        assertNull("Precondition: EMPTY value should have a null 'requireTypeIdForSubtypes'",
                originalValue.getRequireTypeIdForSubtypes());

        // Act: Create a new Value instance by changing the 'requireTypeIdForSubtypes' property.
        final Boolean newRequireTypeIdValue = Boolean.FALSE;
        JsonTypeInfo.Value updatedValue = originalValue.withRequireTypeIdForSubtypes(newRequireTypeIdValue);

        // Assert: The new instance should not be equal to the original,
        // and its property should reflect the change.
        assertNotEquals("The updated value should not be equal to the original", originalValue, updatedValue);
        assertEquals("The 'requireTypeIdForSubtypes' property should be updated to the new value",
                newRequireTypeIdValue, updatedValue.getRequireTypeIdForSubtypes());

        // Also, verify that other properties from the original value are preserved.
        // The original EMPTY value has 'idVisible' as false.
        assertFalse("An unrelated property like 'idVisible' should be preserved",
                updatedValue.getIdVisible());
    }
}