package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test cases for handling invalid query syntax in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing a selector query that ends with a combinator (e.g., '>', '+', '~')
     * throws an exception, as it is syntactically incomplete. A combinator must be followed
     * by another selector.
     */
    @Test
    public void shouldThrowExceptionWhenQueryEndsWithACombinator() {
        // Arrange: A query that is valid up to the combinator, but is missing the subsequent selector.
        String invalidQuery = ":is(:eq(1114)) ~ ";
        String expectedErrorMessage = "Could not parse query ':is(:eq(1114)) ~': unexpected token at ''";

        // Act & Assert
        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown for a query with a trailing combinator.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is informative and points to the root cause.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}