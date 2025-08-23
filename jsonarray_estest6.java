package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class, focusing on the getAsString() method.
 */
public class JsonArrayTest {

    @Test
    public void getAsString_onArrayWithSingleNumber_returnsNumberAsString() {
        // Arrange: Create a JsonArray and add a single integer element.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(-3);

        // Act: Call the method under test.
        String result = jsonArray.getAsString();

        // Assert: Verify that the result is the string representation of the number.
        assertEquals("-3", result);
    }
}