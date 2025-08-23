package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link QueryParser}.
 * This test focuses on parsing attribute selectors with special characters.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser can handle an attribute selector where the key and value
     * contain characters that might be mistaken for format specifiers (e.g., "%s").
     * The parser should treat these characters as literal parts of the string.
     */
    @Test
    public void shouldParseAttributeSelectorContainingSpecialCharacters() {
        // Arrange: An attribute selector with a "contains" match ('*=').
        // The key and value are both the literal string "%s".
        String query = "[%s*=%s]";

        // Act: Parse the selector query.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: The parser should successfully create an evaluator.
        assertNotNull("The parsed evaluator should not be null.", evaluator);

        // A more specific assertion: The string representation of the resulting
        // evaluator should match the original query, confirming that the key,
        // operator, and value were parsed correctly.
        assertEquals("[%s*=%s]", evaluator.toString());
    }
}