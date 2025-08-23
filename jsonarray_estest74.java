package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on its convenience getter methods.
 */
public class JsonArrayTest {

    @Test
    public void getAsBoolean_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();

        // Act & Assert: Verify that calling getAsBoolean() throws the expected exception.
        try {
            emptyArray.getAsBoolean();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the exception message correctly states the reason for the failure.
            assertEquals("Array must have size 1, but has size 0", e.getMessage());
        }
    }
}