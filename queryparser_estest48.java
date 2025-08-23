package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link QueryParser}.
 * This class contains the improved test case.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles a selector query that begins with a child combinator ('>').
     * <p>
     * A leading combinator implies that the first element in the sequence must be a direct child of the document root.
     * This test uses an unusual name ("mttchesWholeOw") to also ensure the parser correctly identifies it as a tag
     * and not a malformed pseudo-selector.
     */
    @Test
    public void parseShouldHandleQueryStartingWithChildCombinator() {
        // Arrange
        // The query selects a "Text" element that is a direct child of a
        // "mttchesWholeOw" element, which in turn is a direct child of the root.
        String query = ">mttchesWholeOw>Text";
        String expectedParsedStructure = "> mttchesWholeOw > Text";

        // Act
        Evaluator evaluator = QueryParser.parse(query);

        // Assert
        // The original test only checked for a non-null result. This improved assertion
        // verifies that the query was parsed into the correct evaluator structure by
        // checking its string representation.
        assertEquals(expectedParsedStructure, evaluator.toString());
    }
}