package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Tests that the {@code :matchText} pseudo-selector is parsed correctly,
     * and the resulting Evaluator's string representation matches the original query.
     * This pseudo-selector is a Jsoup-specific extension.
     */
    @Test
    public void parseShouldCorrectlyHandleMatchTextPseudoSelector() {
        // Arrange
        String query = ":matchText";

        // Act
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        assertEquals("The evaluator's string representation should match the original query.",
                query, evaluator.toString());
    }
}