package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link QueryParser} class.
 */
class QueryParserTest {

    /**
     * Verifies that the QueryParser correctly trims leading and trailing whitespace
     * from the input query string before parsing.
     */
    @Test
    @DisplayName("should trim leading and trailing whitespace from a query")
    void parseTrimsLeadingAndTrailingWhitespace() {
        // Arrange: Define a query with extra whitespace at the beginning and end.
        String queryWithExtraSpaces = " span div  ";
        String expectedParsedQuery = "span div";

        // Act: Parse the query using the method under test.
        Evaluator evaluator = QueryParser.parse(queryWithExtraSpaces);

        // Assert: Verify that the resulting evaluator's string representation
        // matches the query with whitespace trimmed.
        assertEquals(expectedParsedQuery, evaluator.toString());
    }
}