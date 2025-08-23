package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that an instance of JsonIgnoreProperties.Value is not equal to null,
     * adhering to the general contract of the Object.equals() method.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithNull() {
        // Arrange: Create an empty instance of JsonIgnoreProperties.Value
        JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.empty();

        // Act & Assert: The equals() method must return false for a null argument.
        assertFalse("An object instance should never be equal to null.", emptyValue.equals(null));
    }
}