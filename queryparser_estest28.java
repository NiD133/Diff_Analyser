package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link QueryParser}.
 * This class focuses on testing invalid query parsing scenarios.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing a query with an unexpected closing parenthesis
     * throws an IllegalStateException with a descriptive message.
     */
    @Test
    public void parse_whenQueryContainsUnexpectedClosingParenthesis_throwsIllegalStateException() {
        // Arrange: Define an invalid CSS query. A simple query with an unmatched ')'
        // is clearer than a complex, randomly generated one.
        String invalidQuery = "div)";

        // Act & Assert: Use assertThrows to verify that the expected exception is thrown.
        // This is a modern, concise, and readable way to test for exceptions.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> QueryParser.parse(invalidQuery)
        );

        // Assert: Verify the exception message is correct and helpful.
        // This ensures users get clear feedback on why their query failed.
        String expectedMessage = "Could not parse query 'div)': unexpected token at ')'";
        assertEquals(expectedMessage, exception.getMessage());
    }
}