package org.jsoup.select;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link QueryParser}, focusing on error handling for invalid selector syntax.
 */
public class QueryParserTest {

    // The ExpectedException rule allows us to declaratively specify what exception we expect to be thrown.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parse_shouldThrowExceptionForMalformedQueryWithUnexpectedToken() {
        // Arrange: Define a syntactically incorrect CSS query.
        // The query "RV,L fO: 4},X" is invalid because after the selector list "RV,L",
        // the subsequent token " fO: 4},X" is not a valid selector component.
        // The parser expects a new selector (e.g., a tag, #id, or .class) but finds an unrecognized token.
        String malformedQuery = "RV,L fO: 4},X";
        String expectedErrorMessage = "Could not parse query 'RV,L fO: 4},X': unexpected token at ' fO: 4},X'";

        // Expect: An IllegalStateException with a specific message detailing the parsing error.
        // This makes the test's purpose clear before the action is even performed.
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(expectedErrorMessage);

        // Act: Attempt to parse the malformed query, which should trigger the expected exception.
        QueryParser.parse(malformedQuery);
    }
}