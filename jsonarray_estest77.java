package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the {@link JsonArray} class, focusing on its accessor methods.
 */
public class JsonArrayTest {

    @Test
    public void getAsLong_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        String expectedErrorMessage = "Array must have size 1, but has size 0";

        // Act & Assert
        try {
            emptyArray.getAsLong();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}