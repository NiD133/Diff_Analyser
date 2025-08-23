package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that the equals() method returns false when a JsonLocation instance
     * is compared with an object of an incompatible type. This is a fundamental
     * contract of the equals() method.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        JsonLocation location = JsonLocation.NA;
        Object otherObject = new Object();

        // Act
        boolean areEqual = location.equals(otherObject);

        // Assert
        assertFalse("A JsonLocation instance should not be equal to an object of a different type.", areEqual);
    }
}