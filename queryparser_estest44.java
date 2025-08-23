package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link QueryParser}.
 * This test focuses on parsing simple pseudo-selectors.
 */
public class QueryParserTest {

    /**
     * Verifies that the {@code :first-child} pseudo-selector is parsed correctly.
     * The test ensures that the {@code toString()} representation of the resulting
     * {@link Evaluator} matches the original query string.
     */
    @Test
    public void shouldCorrectlyParseFirstChildPseudoSelector() {
        // Arrange: Define the CSS query for the :first-child pseudo-selector.
        String query = ":first-child";

        // Act: Parse the query to create an Evaluator.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: The string representation of the parsed evaluator should match the original query.
        assertEquals(query, evaluator.toString());
    }
}