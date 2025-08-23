package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class, focusing on convenience getter methods.
 */
public class JsonArrayTest {

    /**
     * Verifies that getAsFloat() correctly returns the float value
     * when the array contains a single element that is a character representing a digit.
     */
    @Test
    public void getAsFloat_whenArrayContainsSingleCharacterDigit_returnsCorrespondingFloat() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6'); // The character '6' is added to the array

        // Act
        float actualFloat = jsonArray.getAsFloat();

        // Assert
        float expectedFloat = 6.0F;
        assertEquals(expectedFloat, actualFloat, 0.01F);
    }
}