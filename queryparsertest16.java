package org.jsoup.select;

import org.junit.jupiter.api.Test;

import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for invalid query syntax handling in {@link QueryParser}.
 * This suite focuses on queries that are syntactically incorrect and should throw a SelectorParseException.
 */
public class QueryParserSyntaxErrorTest {

    @Test
    void parsingQueryWithConsecutiveChildCombinatorsThrowsException() {
        // Arrange: An invalid query with two consecutive child combinators (>>)
        String invalidQuery = "div>>p";
        String expectedErrorMessage = "Could not parse query 'div>>p': unexpected token at '>p'";

        // Act: The parsing action is expected to throw an exception
        SelectorParseException thrown = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(invalidQuery)
        );

        // Assert: The exception message is as expected
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    void parsingQueryWithConsecutiveAdjacentCombinatorsThrowsException() {
        // Arrange: An invalid query with two consecutive adjacent sibling combinators (+ +)
        String invalidQuery = "+ + div";
        String expectedErrorMessage = "Could not parse query '+ + div': unexpected token at '+ div'";

        // Act: The parsing action is expected to throw an exception
        SelectorParseException thrown = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(invalidQuery)
        );

        // Assert: The exception message is as expected
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}