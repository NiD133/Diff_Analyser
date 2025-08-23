package org.jsoup.select;

import org.jsoup.select.Selector.SelectorParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for invalid query syntax in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing a CSS selector with a trailing combinator (e.g., "div ~")
     * throws a SelectorParseException, as a selector is expected to follow the combinator.
     */
    @Test
    public void parse_selectorWithTrailingCombinator_throwsSelectorParseException() {
        // Arrange: An invalid query with a combinator at the end.
        String invalidQuery = "div ~";

        try {
            // Act
            QueryParser.parse(invalidQuery);
            fail("A SelectorParseException should have been thrown for a query with a trailing combinator.");
        } catch (SelectorParseException e) {
            // Assert: The exception is caught and its message is as expected.
            assertEquals("Expected selector after combinator '~'", e.getMessage());
        }
    }
}