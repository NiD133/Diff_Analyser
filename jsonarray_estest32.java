package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on exception handling for getter methods.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsLong() on a JsonArray containing a single,
     * non-numeric character element throws a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void getAsLong_shouldThrowNumberFormatException_whenArrayContainsNonNumericCharacter() {
        // Arrange: Create a JsonArray and add a single element that cannot be parsed as a long.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('~');

        // Act: Attempt to get the element as a long.
        // This call is expected to throw a NumberFormatException.
        jsonArray.getAsLong();

        // Assert: The test passes if the expected NumberFormatException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}