package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void getAsDouble_whenArrayContainsSingleCharacterDigit_returnsDoubleValue() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6');

        // Act
        double result = jsonArray.getAsDouble();

        // Assert
        assertEquals(6.0, result, 0.01);
    }
}