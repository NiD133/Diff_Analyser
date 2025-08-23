package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its equality implementation.
 */
public class JsonLocationTest {

    /**
     * Verifies that the equals() method returns false when comparing a JsonLocation
     * instance to null. This adheres to the general contract of Object.equals().
     */
    @Test
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // Arrange: Get a standard instance of JsonLocation.
        // JsonLocation.NA is a convenient, non-null, static instance to use.
        JsonLocation location = JsonLocation.NA;

        // Act & Assert: The result of comparing any object to null should be false.
        assertFalse("A JsonLocation instance should not be equal to null.", location.equals(null));
    }
}