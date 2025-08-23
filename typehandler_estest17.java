package org.apache.commons.cli;

import org.junit.Test;

/**
 * Unit tests for the {@link TypeHandler} class.
 */
public class TypeHandlerTest {

    /**
     * Tests that createNumber() throws a ParseException when given a string
     * that cannot be parsed into a number.
     */
    @Test(expected = ParseException.class)
    public void createNumberShouldThrowParseExceptionForNonNumericString() throws ParseException {
        // Arrange: A string that is not a valid number.
        final String nonNumericString = "I%5HIg?GFqZ!&";

        // Act & Assert: Calling createNumber with the invalid string should throw a ParseException.
        TypeHandler.createNumber(nonNumericString);
    }
}