package com.google.gson;

import org.junit.Test;

/**
 * Contains tests for the {@link JsonArray#getAsDouble()} method.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsDouble() on a JsonArray containing a single,
     * non-numeric string element throws a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void getAsDouble_withSingleNonNumericString_throwsNumberFormatException() {
        // Arrange: Create a JsonArray and add a string that cannot be parsed as a double.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("not a valid double");

        // Act: Attempt to get the element as a double.
        // This call is expected to throw a NumberFormatException.
        jsonArray.getAsDouble();

        // Assert: The test succeeds if the expected NumberFormatException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}