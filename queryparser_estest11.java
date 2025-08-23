package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the Jsoup {@link QueryParser}.
 * This test focuses on parsing the ':matchesWholeText' pseudo-selector.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles the ':matchesWholeText' pseudo-selector
     * when its argument contains characters that resemble a format specifier.
     * The parser should treat the argument as a literal string for the regex pattern.
     */
    @Test
    public void shouldParseMatchesWholeTextWithFormatSpecifierLikeArgument() {
        // Arrange: Define a query with a regex argument that looks like a format specifier.
        // The parser should interpret "(%s)" as a valid regex pattern.
        String query = ":matchesWholeText(%s)";

        // Act: Parse the selector query.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: Verify that a valid evaluator of the correct type was created.
        assertNotNull("The parsed evaluator should not be null.", evaluator);
        assertTrue("The evaluator should be an instance of MatchesWholeText.",
                evaluator instanceof Evaluator.MatchesWholeText);
    }
}