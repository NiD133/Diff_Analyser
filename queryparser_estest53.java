package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link QueryParser} class.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles a query with a pseudo-selector
     * followed by a series of descendant selectors separated by spaces.
     * This ensures that spaces are correctly interpreted as descendant combinators
     * even after a complex pseudo-selector like `:containsData(text)`.
     */
    @Test
    public void parsesQueryWithDescendantSelectorsAfterPseudoSelector() {
        // Arrange
        // The query string contains a pseudo-selector followed by what could be mistaken
        // for arbitrary text. The parser should treat this as a valid chain of
        // descendant selectors (`query`, `must`, `not`, `be`, `empty`).
        String query = ":containsData(text) query must not be empty";

        // Act
        // Parsing this query should complete successfully without throwing a SelectorParseException.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        // A non-null result confirms that the parser produced a valid evaluator.
        assertNotNull("Parsing a valid query with descendant selectors after a pseudo-selector should not fail.", evaluator);
    }
}