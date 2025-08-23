package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link QueryParser}, focusing on its handling of complex or malformed queries.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser leniently handles a query containing extraneous text
     * after a valid pseudo-selector. The parser is expected to interpret the
     * subsequent unquoted words as a chain of descendant tag selectors rather
     * than throwing an exception.
     */
    @Test
    public void parse_withExtraneousTextAfterPseudoSelector_treatsTextAsTagSelectors() {
        // Arrange: A query that starts with a valid pseudo-selector but is followed
        // by what appears to be a plain-text sentence.
        String malformedQuery = ":not(selector) subselect must not be empty";

        // Act: Parse the query.
        Evaluator evaluator = QueryParser.parse(malformedQuery);

        // Assert: The parser should successfully create an Evaluator object. More importantly,
        // the string representation of the parsed evaluator should match the original query,
        // confirming that the "garbage" text was interpreted as a valid selector chain.
        assertNotNull("The parser should not return null for a parsable, albeit malformed, query.", evaluator);
        assertEquals("The parsed evaluator's structure should match the lenient interpretation of the input query.",
            malformedQuery, evaluator.toString());
    }
}