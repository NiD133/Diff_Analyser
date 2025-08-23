package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link QueryParser}.
 * This test focuses on the correct parsing of CSS pseudo-selectors.
 */
public class QueryParserTest {

    /**
     * Verifies that the {@code :first-of-type} pseudo-selector is parsed correctly.
     * The test ensures that the resulting Evaluator's string representation
     * matches the original query string.
     */
    @Test
    public void shouldParseFirstOfTypePseudoSelector() {
        // Arrange
        String query = ":first-of-type";

        // Act
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        assertEquals(query, evaluator.toString());
    }
}