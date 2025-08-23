package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link QueryParser} focusing on handling invalid query syntax.
 */
public class QueryParserTest {

    @Test
    public void parse_whenQueryEndsWithDanglingCombinator_throwsException() {
        // GIVEN: An invalid CSS query that ends with a dangling combinator.
        // A combinator (like the general sibling '~') must be followed by another selector sequence.
        String invalidQuery = ":nth-of-type(16n+24) ~ ";

        // WHEN: The invalid query is parsed.
        // THEN: The parser should throw an exception because it expects another selector
        // component after the combinator but instead finds the end of the string.
        IllegalStateException exception = assertThrows(
            "Parsing should fail for a query ending with a combinator.",
            IllegalStateException.class,
            () -> QueryParser.parse(invalidQuery)
        );

        // AND: The exception message should reflect the specific internal parsing failure.
        // Note: This assertion is tied to the current implementation's internal error
        // message, which originates from a failing regex Matcher.
        assertEquals("No match found", exception.getMessage());
    }
}