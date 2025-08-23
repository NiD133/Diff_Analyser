package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the error-handling capabilities of the {@link QueryParser}.
 * This focuses on ensuring the parser fails gracefully with informative messages
 * for invalid CSS query syntax.
 */
public class QueryParserErrorHandlingTest {

    /**
     * Verifies that parsing a query with an unexpected token immediately following a valid attribute selector
     * throws an {@link IllegalStateException}. According to CSS selector grammar, a combinator (like ' ', '>', '+'),
     * another selector, or the end of the query is expected after an attribute selector.
     */
    @Test
    public void parseThrowsExceptionForUnexpectedTokenAfterAttributeSelector() {
        // The query is invalid because the underscore '_' is not a valid token
        // to follow the attribute selector "[b]".
        String invalidQuery = "wj[b]_o4~M";

        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown due to invalid query syntax, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // The exception message should be specific, identifying the invalid part of the query.
            String expectedMessage = "Could not parse query 'wj[b]_o4~M': unexpected token at '_o4~M'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}