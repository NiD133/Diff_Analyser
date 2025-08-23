package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on type conversion methods.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsBigInteger() on a JsonArray containing a single
     * boolean element results in a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void getAsBigInteger_whenArrayContainsSingleBoolean_shouldThrowNumberFormatException() {
        // Arrange: Create a JsonArray and add a single boolean value.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);

        // Act & Assert: Attempting to get the value as a BigInteger should throw
        // a NumberFormatException because "true" cannot be parsed as a number.
        jsonArray.getAsBigInteger();
    }
}