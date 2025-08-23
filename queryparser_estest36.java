package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the CSS selector parser in {@link QueryParser}.
 */
public class QueryParserTest {

    @Test
    public void parsesRootPseudoSelectorCorrectly() {
        // Arrange: Define the CSS query for the :root pseudo-selector.
        String query = ":root";

        // Act: Parse the query to get an Evaluator.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: The string representation of the resulting evaluator should match the original query.
        // This confirms that the :root pseudo-selector was parsed into the correct Evaluator type.
        assertEquals(":root", evaluator.toString());
    }
}