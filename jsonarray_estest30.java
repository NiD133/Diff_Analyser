package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Contains tests for the {@link JsonArray} class, focusing on its type conversion methods.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsShort() on a JsonArray containing a single,
     * non-numeric element throws a NumberFormatException.
     */
    @Test
    public void getAsShort_whenElementIsNotANumber_throwsNumberFormatException() {
        // Arrange: Create a JsonArray with a single element that cannot be parsed as a short.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('@');

        // Act & Assert: Verify that calling getAsShort() throws the expected exception.
        // The method should fail because "@" is not a valid representation of a number.
        NumberFormatException exception = assertThrows(
            NumberFormatException.class,
            () -> jsonArray.getAsShort()
        );

        // Assert on the exception message for a more specific and robust test.
        assertEquals("For input string: \"@\"", exception.getMessage());
    }
}