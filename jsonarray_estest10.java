package com.google.gson;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for the {@link JsonArray} class, focusing on its convenience getter methods.
 */
public class JsonArrayTest {

    /**
     * Tests that getAsLong() on an array with a single float element
     * returns the long value of that element, truncating the decimal part.
     */
    @Test
    public void getAsLong_whenArrayContainsSingleFloat_returnsTruncatedLongValue() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        // Add a float value that will be truncated upon conversion to long.
        // Using 0.9f is more explicit about testing truncation than 0.0f.
        jsonArray.add(0.9f);

        // Act
        long result = jsonArray.getAsLong();

        // Assert
        // The method should convert the float to a long, which truncates the decimal part.
        assertEquals(0L, result);
    }
}