package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class would typically contain all tests for the JsonArray class.
 * This example focuses on refactoring a single, auto-generated test case for clarity.
 */
public class JsonArrayTest {

    /**
     * Verifies that getAsCharacter() correctly returns the primitive char
     * when the JsonArray contains a single character element.
     */
    @Test
    public void getAsCharacter_whenArrayContainsSingleCharacter_returnsTheCharacter() {
        // Arrange: Create a JsonArray and add a single character to it.
        JsonArray jsonArray = new JsonArray();
        char expectedCharacter = '6';
        jsonArray.add(expectedCharacter); // Autoboxing converts char to Character

        // Act: Call the method under test.
        char actualCharacter = jsonArray.getAsCharacter();

        // Assert: Verify that the returned character is the one we added.
        assertEquals(expectedCharacter, actualCharacter);
    }
}