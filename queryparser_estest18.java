package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link QueryParser}.
 * This test focuses on parsing attribute selectors.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles an attribute selector with the '~=' operator,
     * particularly when the attribute name and value contain non-alphanumeric characters
     * like '%s'.
     *
     * This test improves upon an auto-generated version that only checked for a non-null
     * result. By asserting the string representation of the parsed evaluator, we confirm
     * that the correct type of evaluator was created with the correct attribute key and value.
     */
    @Test
    public void parsesAttributeTildeEqualsSelectorWithSpecialCharactersAsKeyAndValue() {
        // Arrange: A CSS selector for an attribute named "%s" whose value contains the word "%s".
        String query = "[%s~=%s]";

        // Act: Parse the selector string.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: The parser should produce a non-null evaluator that correctly represents
        // the parsed selector. Checking the string representation is a concise and effective
        // way to verify its internal state (key, value, and operator type).
        assertNotNull("The parsed evaluator should not be null.", evaluator);
        assertEquals("The evaluator's string representation should match the original query.",
                query, evaluator.toString());
    }
}