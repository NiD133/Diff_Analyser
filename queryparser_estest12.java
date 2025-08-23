package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the :matchesOwn pseudo-selector can be parsed correctly,
     * even when its argument contains a string that resembles a format specifier ("%s").
     * The parser should treat the argument as a literal regular expression pattern
     * without throwing an exception.
     */
    @Test
    public void parsesMatchesOwnWithFormatSpecifierArgument() {
        // Arrange: Define a CSS query with a regex pattern that looks like a format specifier.
        String queryWithFormatSpecifier = ":matchesOwn(%s)";

        // Act: Parse the query to create an Evaluator.
        Evaluator evaluator = QueryParser.parse(queryWithFormatSpecifier);

        // Assert: Verify that the parsing was successful and created the correct type of evaluator.
        assertNotNull("The parser should return a non-null evaluator for a valid query.", evaluator);
        
        // A more specific check ensures the correct evaluator for ':matchesOwn' is created.
        assertTrue("The evaluator should be an instance of Evaluator.MatchesOwn.",
                   evaluator instanceof Evaluator.MatchesOwn);
    }
}