package com.google.gson;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for {@link JsonArray}.
 */
public class JsonArrayTest {

    @Test
    public void getAsByte_shouldThrowNumberFormatException_whenElementIsNotAValidByte() {
        // Arrange: Create a JsonArray with a single element that is a non-numeric string.
        JsonArray jsonArray = new JsonArray();
        String nonNumericString = "not a byte";
        jsonArray.add(nonNumericString);

        // Act & Assert: Verify that calling getAsByte() throws NumberFormatException.
        NumberFormatException exception = assertThrows(
            NumberFormatException.class,
            () -> jsonArray.getAsByte(),
            "Expected getAsByte() to throw, but it did not"
        );

        // Assert on the exception message for more specific verification.
        assertEquals("For input string: \"" + nonNumericString + "\"", exception.getMessage());
    }
}