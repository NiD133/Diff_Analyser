package com.google.gson;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Contains tests for the {@link JsonArray#getAsNumber()} method.
 */
public class JsonArrayGetAsNumberTest {

    /**
     * Verifies that getAsNumber() correctly converts an array
     * with a single character digit into the corresponding Number.
     */
    @Test
    public void getAsNumberShouldReturnCorrectNumberForSingleCharacterDigitElement() {
        // Arrange: Create a JsonArray containing a single character '6'.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6');

        // Act: Retrieve the element as a Number.
        Number resultNumber = jsonArray.getAsNumber();

        // Assert: The resulting Number's value should be 6.
        assertEquals("The number should be parsed from the character digit", 6, resultNumber.intValue());
    }
}