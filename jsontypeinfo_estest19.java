package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link JsonTypeInfo.Value} class, focusing on its immutability and equality logic.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that using withInclusionType() creates a new, distinct Value instance
     * when a different inclusion type is provided.
     */
    @Test
    public void withInclusionType_shouldCreateUnequalInstance_whenTypeIsDifferent() {
        // Arrange: Start with an empty Value instance.
        final JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.EMPTY;
        final JsonTypeInfo.As newInclusionType = JsonTypeInfo.As.WRAPPER_OBJECT;

        // Act: Create a new instance with a modified inclusion type.
        JsonTypeInfo.Value modifiedValue = originalValue.withInclusionType(newInclusionType);

        // Assert: The new instance should be different from the original.
        assertNotEquals("The modified value should not be equal to the original", originalValue, modifiedValue);

        // Also, verify that the inclusion type was correctly updated in the new instance.
        assertEquals("The inclusion type should be updated", newInclusionType, modifiedValue.getInclusionType());

        // And confirm that other properties were not changed.
        assertEquals("The ID type should remain unchanged", originalValue.getIdType(), modifiedValue.getIdType());
        assertFalse("The ID visibility should remain unchanged", modifiedValue.getIdVisible());
    }
}