package com.google.gson;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the {@link JsonArray} class, focusing on the getAsString method.
 */
public class JsonArrayTest {

    /**
     * Tests that getAsString() correctly returns the string value
     * when the JsonArray contains exactly one element which is a string.
     */
    @Test
    public void getAsString_shouldReturnStringValue_whenArrayContainsSingleStringElement() {
        // Arrange: Create a JsonArray and add a single string element.
        JsonArray jsonArray = new JsonArray();
        String expectedString = "";
        jsonArray.add(expectedString);

        // Act: Retrieve the string value from the array.
        String actualString = jsonArray.getAsString();

        // Assert: Verify that the retrieved string matches the element that was added.
        assertEquals("The returned string should match the single element in the array.", expectedString, actualString);
    }
}