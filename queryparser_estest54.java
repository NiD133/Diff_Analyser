package org.jsoup.select;

import org.jsoup.select.Selector.SelectorParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link QueryParser}.
 * This class focuses on validating how the parser handles invalid CSS selector syntax.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing a query with an empty :has() pseudo-selector throws an exception.
     * The :has(selector) syntax requires a non-empty selector argument within the parentheses.
     */
    @Test
    public void parsingEmptyHasSelectorThrowsException() {
        // GIVEN: A query with an empty and therefore invalid :has() pseudo-selector.
        // The original test used a more complex string (":has() must have a selector").
        // This is simplified to the minimal failing case for a more focused test.
        String invalidQuery = ":has()";

        // WHEN & THEN: Parsing the invalid query is expected to throw a SelectorParseException.
        // Using assertThrows provides a clear and concise way to verify exception-throwing behavior.
        // This is a JUnit 5 construct, but a similar approach can be achieved with JUnit 4's @Rule ExpectedException.
        SelectorParseException thrown = assertThrows(SelectorParseException.class, () -> {
            QueryParser.parse(invalidQuery);
        });

        // AND: The exception message should clearly state the reason for the failure.
        assertEquals(":has(selector) must not be empty", thrown.getMessage());
    }
}