package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link JsonArray#getAsCharacter()}.
 */
public class JsonArray_ESTestTest19 {

    /**
     * Tests that getAsCharacter() on an array with a single number element
     * returns the first character of that number's string representation.
     */
    @Test
    public void getAsCharacter_whenArrayContainsSingleNumber_returnsFirstCharOfStringRepresentation() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(123.45); // Using a number whose first digit is not 0

        // Act
        char result = jsonArray.getAsCharacter();

        // Assert
        // The method should convert the number 123.45 to its string form "123.45"
        // and return the first character.
        assertEquals('1', result);
    }
}