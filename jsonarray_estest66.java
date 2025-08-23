package com.google.gson;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the {@link JsonArray} class, focusing on its convenience getter methods.
 */
public class JsonArrayTest {

    @Test
    public void getAsBigDecimalShouldThrowIllegalStateExceptionWhenArrayIsEmpty() {
        // Arrange: Create an empty JsonArray instance.
        JsonArray emptyArray = new JsonArray();
        String expectedErrorMessage = "Array must have size 1, but has size 0";

        // Act & Assert: Verify that calling getAsBigDecimal() on an empty array
        // throws an IllegalStateException with the expected message.
        IllegalStateException thrownException = assertThrows(
            IllegalStateException.class,
            () -> emptyArray.getAsBigDecimal(),
            "Expected getAsBigDecimal() to throw, but it did not"
        );

        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }
}