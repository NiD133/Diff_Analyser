package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the CSS selector parser in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the {@code :last-child} pseudo-selector is parsed correctly.
     * The test ensures that the resulting {@link Evaluator} object can be represented
     * as the original query string.
     */
    @Test
    public void shouldCorrectlyParseLastChildPseudoSelector() {
        // Arrange
        String query = ":last-child";

        // Act
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        assertEquals("The evaluator's string representation should match the original query.",
            query, evaluator.toString());
    }
}