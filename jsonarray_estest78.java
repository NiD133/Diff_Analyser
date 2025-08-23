package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsFloat() on an empty JsonArray throws an IllegalStateException.
     * The method is designed to work only on arrays with exactly one element.
     */
    @Test
    public void getAsFloat_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();
        String expectedMessage = "Array must have size 1, but has size 0";

        // Act & Assert: Call getAsFloat() and verify the exception.
        IllegalStateException thrownException = assertThrows(
            "Calling getAsFloat() on an empty array should throw an IllegalStateException.",
            IllegalStateException.class,
            () -> emptyArray.getAsFloat()
        );

        // Assert: Verify the exception message is correct.
        assertEquals(expectedMessage, thrownException.getMessage());
    }
}