package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link QueryParser}.
 */
class QueryParserTest {

    @Test
    @DisplayName("parse() should throw a SelectorParseException for an empty query")
    void parse_withEmptyQuery_throwsException() {
        // Arrange
        String emptyQuery = "";
        String expectedErrorMessage = "String must not be empty";

        // Act & Assert
        // Verify that parsing an empty string throws the expected exception.
        SelectorParseException thrown = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(emptyQuery)
        );

        // Verify that the exception message is correct.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}