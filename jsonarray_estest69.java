package com.google.gson;

import org.junit.Test;

/**
 * Tests for {@link JsonArray#get(int)}.
 */
public class JsonArrayTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_onEmptyArray_throwsIndexOutOfBoundsException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();

        // Act
        // Attempt to access the first element of an empty array
        emptyArray.get(0);

        // Assert
        // The test expects an IndexOutOfBoundsException, which is handled by the
        // @Test(expected=...) annotation. If no exception or a different one is
        // thrown, the test will fail.
    }
}