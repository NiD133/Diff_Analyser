package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the CSS selector parser in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the ':only-of-type' pseudo-selector is parsed correctly.
     * The resulting Evaluator's string representation should match the input query.
     */
    @Test
    public void shouldParseOnlyOfTypePseudoSelector() {
        // Arrange
        String query = ":only-of-type";

        // Act
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        assertEquals(query, evaluator.toString());
    }
}