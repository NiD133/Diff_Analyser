package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsNumber() on an empty JsonArray throws an IllegalStateException.
     * A JsonArray must contain exactly one element to be converted to a Number.
     */
    @Test
    public void getAsNumber_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an empty JsonArray instance.
        JsonArray emptyArray = new JsonArray();

        // Act & Assert: Verify that calling getAsNumber() throws the expected exception.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> emptyArray.getAsNumber()
        );

        // Assert: Check if the exception message is correct, confirming the reason for the failure.
        assertEquals("Array must have size 1, but has size 0", exception.getMessage());
    }
}