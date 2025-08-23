package org.jsoup.select;

import org.jsoup.select.Selector.SelectorParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for invalid inputs in {@link QueryParser}.
 */
class QueryParserTest {

    @Test
    void parse_withNullQuery_throwsSelectorParseException() {
        // The QueryParser's `parse` method is expected to validate its input.
        // A null query is an invalid argument and should result in an exception.
        
        SelectorParseException thrown = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(null),
            "Parsing a null query should throw a SelectorParseException."
        );

        assertEquals("String must not be empty", thrown.getMessage());
    }
}