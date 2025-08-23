package org.jsoup.select;

import org.jsoup.select.Evaluator.ContainsText;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Jsoup {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser can handle a :contains pseudo-selector
     * whose argument contains special characters, like a format specifier ("%s").
     * This ensures the parser is robust and doesn't misinterpret such inputs.
     */
    @Test
    public void parseHandlesContainsWithFormatSpecifierArgument() {
        // Arrange: A CSS query with a format specifier in the :contains() argument.
        String query = ":contains(%s)";

        // Act: Parse the query into an Evaluator.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: The parser should successfully create a non-null ContainsText evaluator.
        assertNotNull("The parsed evaluator should not be null.", evaluator);
        assertTrue("The evaluator should be an instance of ContainsText.", evaluator instanceof ContainsText);
    }
}