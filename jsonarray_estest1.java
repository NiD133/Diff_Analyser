package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void addNumber_increasesArraySizeByOne() {
        // Arrange
        JsonArray jsonArray = new JsonArray();

        // Act
        jsonArray.add(0.0f);

        // Assert
        assertEquals("The size of the array should be 1 after adding a number.", 1, jsonArray.size());
    }
}