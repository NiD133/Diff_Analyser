package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies the reflexive property of the equals() method,
     * ensuring that an instance is always equal to itself.
     */
    @Test
    public void equalsShouldBeReflexive() {
        // Arrange: Get the constant 'Not Applicable' location instance.
        JsonLocation naLocation = JsonLocation.NA;

        // Assert: An object must be equal to itself.
        // This is a fundamental contract of the Object.equals() method.
        assertEquals("A JsonLocation instance should be equal to itself.", naLocation, naLocation);
    }
}