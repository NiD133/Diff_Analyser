package com.google.gson;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void getAsBigDecimal_shouldReturnCorrectValue_whenArrayContainsSingleNumericCharacter() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('6');
        BigDecimal expectedBigDecimal = new BigDecimal("6");

        // Act
        BigDecimal actualBigDecimal = jsonArray.getAsBigDecimal();

        // Assert
        assertEquals(expectedBigDecimal, actualBigDecimal);
    }
}