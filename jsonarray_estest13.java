package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// Note: In a real-world scenario, this class would be named `JsonArrayTest`.
public class JsonArray_ESTestTest13 {

    /**
     * Verifies that getAsFloat() returns the correct float value when the JsonArray
     * contains a single element that can be represented as a float.
     */
    @Test
    public void getAsFloat_shouldReturnFloatValue_whenArrayContainsSingleFloatElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        float expectedFloat = 0.0f;
        jsonArray.add(expectedFloat);

        // Act
        float actualFloat = jsonArray.getAsFloat();

        // Assert
        // A delta of 0.0f is used for an exact match in a float comparison.
        assertEquals(expectedFloat, actualFloat, 0.0f);
    }
}