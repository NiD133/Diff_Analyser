package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the immutable {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the withIdType() method creates a new Value instance
     * with the specified Id type, while leaving the original instance unchanged.
     */
    @Test
    public void withIdType_shouldCreateNewValueWithSpecifiedIdType() {
        // Arrange: Start with a default Value instance and define the desired ID type.
        final JsonTypeInfo.Value initialValue = JsonTypeInfo.Value.EMPTY;
        final JsonTypeInfo.Id newIdType = JsonTypeInfo.Id.CLASS;

        // Act: Create a new Value instance by calling the "wither" method.
        final JsonTypeInfo.Value updatedValue = initialValue.withIdType(newIdType);

        // Assert: Check the properties of both the new and original instances.
        
        // 1. Verify the new instance has the correct properties.
        assertNotSame("A new instance should be created", initialValue, updatedValue);
        assertEquals("The ID type should be updated", newIdType, updatedValue.getIdType());
        assertFalse("Other properties should retain their default value", updatedValue.getIdVisible());

        // 2. Verify the original instance remains unchanged (immutability).
        assertEquals("Original instance ID type should not change", JsonTypeInfo.Id.NONE, initialValue.getIdType());
    }
}