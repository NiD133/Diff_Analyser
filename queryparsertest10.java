package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for parsing error handling in {@link QueryParser}.
 */
class QueryParserTest {

    @Test
    @DisplayName("Parsing should fail for an unsupported combinator like '/'")
    void throwsExceptionForUnsupportedCombinator() {
        // Arrange: Define a query with an invalid combinator and the expected error message.
        // The '/' character is not a valid CSS selector combinator.
        String invalidQuery = "div / foo";
        String expectedMessage = "Could not parse query 'div / foo': unexpected token at '/ foo'";

        // Act & Assert: Verify that parsing the invalid query throws a SelectorParseException.
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(invalidQuery)
        );

        // Assert that the exception message is clear and helpful.
        assertEquals(expectedMessage, exception.getMessage());
    }
}