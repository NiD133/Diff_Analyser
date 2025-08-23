package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the CSS selector parser in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles an attribute selector with the "not equal"
     * operator (`!=`). This test specifically uses an attribute name and value containing
     * the '%' character, demonstrating the parser's leniency with non-standard identifiers.
     */
    @Test
    public void parsesAttributeNotEqualsSelectorWithUnusualCharacters() {
        // Arrange: A CSS query with an attribute selector using the '!=' operator
        // and non-standard characters for the attribute name and value.
        String query = "[%s!=%s]";

        // Act: Parse the query into an Evaluator.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: The resulting Evaluator should correctly represent the parsed query.
        // Checking the string representation confirms the structure, attribute, operator, and value.
        assertEquals("[%s!=%s]", evaluator.toString());
    }
}