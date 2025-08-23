package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling get() with a negative index on an empty array
     * throws an ArrayIndexOutOfBoundsException.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getWithNegativeIndex_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();

        // Act: Attempt to access an element with a negative index.
        // The @Test annotation will assert that an ArrayIndexOutOfBoundsException is thrown.
        emptyArray.get(-1);
    }
}