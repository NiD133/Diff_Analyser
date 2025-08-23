package com.google.gson;

import org.junit.Test;

/**
 * Tests for {@link JsonArray#getAsBigDecimal()} focusing on invalid input scenarios.
 */
public class JsonArrayTest {

    /**
     * Verifies that getAsBigDecimal() throws a NumberFormatException when the array's
     * single element is a string that cannot be converted to a number.
     */
    @Test(expected = NumberFormatException.class)
    public void getAsBigDecimalShouldThrowNumberFormatExceptionForNonNumericString() {
        // Arrange: Create a JsonArray containing a single, non-numeric string element.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("");

        // Act: Attempt to retrieve the element as a BigDecimal.
        // This action is expected to throw a NumberFormatException.
        jsonArray.getAsBigDecimal();

        // Assert: The test succeeds if the expected NumberFormatException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}