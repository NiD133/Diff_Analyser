package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class, focusing on its convenience getter methods.
 */
public class JsonArrayTest {

    @Test
    public void getAsLong_whenArrayContainsSingleCharacterDigit_returnsCorrespondingLong() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6'); // The character '6' is added, which will be stored as a JsonPrimitive.

        // Act
        long actualLong = jsonArray.getAsLong();

        // Assert
        long expectedLong = 6L;
        assertEquals(expectedLong, actualLong);
    }
}