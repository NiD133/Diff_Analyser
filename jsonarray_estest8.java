package com.google.gson;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link JsonArray}.
 */
public class JsonArrayTest {

    @Test
    public void getAsShort_whenArrayContainsSingleNumericCharacter_returnsCorrespondingShort() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6');

        // Act
        short actualShort = jsonArray.getAsShort();

        // Assert
        short expectedShort = 6;
        assertEquals(expectedShort, actualShort);
    }
}