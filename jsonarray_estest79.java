package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void getAsDouble_onEmptyArray_throwsIllegalStateException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        String expectedErrorMessage = "Array must have size 1, but has size 0";

        // Act & Assert
        IllegalStateException exception = assertThrows(
            "Calling getAsDouble() on an empty array should throw an exception.",
            IllegalStateException.class,
            () -> emptyArray.getAsDouble()
        );

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}