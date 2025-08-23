package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on exception-throwing scenarios.
 */
public class JsonArrayTest {

    @Test
    public void getAsCharacter_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();
        String expectedMessage = "Array must have size 1, but has size 0";

        // Act & Assert: Verify that calling getAsCharacter() throws the expected exception.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> emptyArray.getAsCharacter()
        );

        // Assert: Check if the exception message is correct.
        assertEquals(expectedMessage, exception.getMessage());
    }
}