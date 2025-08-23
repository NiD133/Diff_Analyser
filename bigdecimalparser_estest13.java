package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link BigDecimalParser} class, focusing on its exception-handling capabilities.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that the {@code parse} method throws a {@link NumberFormatException}
     * when given a string that is not a valid number. This test specifically checks
     * an input that starts with an exponent character 'e' but is otherwise malformed.
     */
    @Test
    public void parse_withMalformedNumberString_shouldThrowNumberFormatException() {
        // Arrange: Define an input string that is clearly not a valid number.
        final String invalidNumberString = "eA8ojpN";

        // Act & Assert
        try {
            BigDecimalParser.parse(invalidNumberString);
            // If this line is reached, the test fails because no exception was thrown.
            fail("Expected a NumberFormatException for the invalid input: " + invalidNumberString);
        } catch (NumberFormatException e) {
            // Assert: Verify that the exception message is informative and contains the problematic value.
            String actualMessage = e.getMessage();
            String expectedContent = "can not be deserialized as `java.math.BigDecimal`";

            assertTrue(
                "Exception message should explain the parsing failure. Actual: " + actualMessage,
                actualMessage.contains(expectedContent)
            );
            assertTrue(
                "Exception message should include the invalid input string. Actual: " + actualMessage,
                actualMessage.contains("\"" + invalidNumberString + "\"")
            );
        }
    }
}