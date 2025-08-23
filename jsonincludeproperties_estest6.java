package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class, focusing on its equality logic.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies that the equals() method adheres to the standard Java contract
     * by returning false when the object is compared with null.
     */
    @Test
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // Arrange: Create an instance of JsonIncludeProperties.Value that includes all properties.
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.all();

        // Act & Assert: The result of comparing an object to null must always be false.
        assertFalse("The equals method should return false for a null comparison.", value.equals(null));
    }
}