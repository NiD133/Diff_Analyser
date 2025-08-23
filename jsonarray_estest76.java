package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on exception handling for accessor methods.
 */
public class JsonArrayTest {

    @Test
    public void getAsStringShouldThrowIllegalStateExceptionWhenArrayIsEmpty() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();
        String expectedMessage = "Array must have size 1, but has size 0";

        // Act & Assert: Verify that calling getAsString() on an empty array throws the correct exception.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            emptyArray.getAsString();
        });

        // Assert: Check if the exception message is as expected.
        assertEquals(expectedMessage, exception.getMessage());
    }
}