package com.google.gson;

import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Tests that getAsBigInteger() correctly retrieves the value
     * from a JsonArray containing a single integer element.
     */
    @Test
    public void getAsBigInteger_whenArrayContainsSingleInteger_returnsCorrectBigInteger() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(1);
        BigInteger expectedBigInteger = BigInteger.ONE;

        // Act
        BigInteger actualBigInteger = jsonArray.getAsBigInteger();

        // Assert
        assertEquals(expectedBigInteger, actualBigInteger);
    }
}