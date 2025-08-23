package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the {@code :last-of-type} pseudo-selector is parsed correctly
     * and that its string representation matches the original query.
     */
    @Test
    public void shouldParseLastOfTypePseudoSelector() {
        // Arrange
        String query = ":last-of-type";

        // Act
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        assertEquals("The toString() representation of the parsed evaluator should match the original query.",
                query, evaluator.toString());
    }
}