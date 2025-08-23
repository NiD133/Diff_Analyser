package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling remove() with a negative index throws an 
     * ArrayIndexOutOfBoundsException, as the operation is delegated to an
     * underlying ArrayList which enforces this boundary check.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void remove_withNegativeIndex_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Create an empty JsonArray instance.
        JsonArray jsonArray = new JsonArray();

        // Act: Attempt to remove an element at an invalid, negative index.
        // This action is expected to throw the exception.
        jsonArray.remove(-1);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}