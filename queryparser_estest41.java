package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the CSS selector parsing in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the simple pseudo-selector ":only-child" is parsed correctly.
     * The test ensures that the string representation of the resulting Evaluator
     * matches the original query.
     */
    @Test
    public void shouldParseOnlyChildPseudoSelector() {
        // Arrange: Define the CSS query to be parsed.
        String query = ":only-child";

        // Act: Parse the query to create an Evaluator.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: Verify that the parsed evaluator's string form matches the input.
        assertEquals(query, evaluator.toString());
    }
}