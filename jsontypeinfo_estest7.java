package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class, focusing on its equality contract.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the equals() method returns false when a JsonTypeInfo.Value
     * instance is compared with an object of a different, incompatible type.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        Object otherObject = "a random string"; // An object of a different type

        // Act
        boolean result = value.equals(otherObject);

        // Assert
        assertFalse("equals() must return false for objects of different types.", result);
    }
}