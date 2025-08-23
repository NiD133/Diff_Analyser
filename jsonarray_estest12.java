package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonArray}.
 */
public class JsonArrayTest {

    @Test
    public void getAsInt_whenArrayContainsSingleNumericCharacter_returnsCorrespondingInt() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6');

        // Act
        int result = jsonArray.getAsInt();

        // Assert
        assertEquals(6, result);
    }
}