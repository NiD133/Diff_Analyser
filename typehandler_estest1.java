package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link TypeHandler} class.
 */
public class TypeHandlerTest {

    /**
     * Tests that createNumber() correctly parses a string representing an integer
     * into a Long object.
     *
     * Note: This test covers a method that is marked as @Deprecated.
     *
     * @throws ParseException if the number string is invalid.
     */
    @Test
    public void createNumberShouldReturnLongForIntegerString() throws ParseException {
        // Arrange: Define the input string and the expected result.
        final String integerString = "6";
        final Long expectedValue = 6L;

        // Act: Call the method under test.
        final Number actualValue = TypeHandler.createNumber(integerString);

        // Assert: Verify the result is of the correct type and value.
        assertTrue("The result should be an instance of Long for an integer string.",
                   actualValue instanceof Long);
        assertEquals("The numeric value should match the expected long.",
                     expectedValue, actualValue);
    }
}