package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link QueryParser}, focusing on parsing queries
 * that involve the `:has` pseudo-selector with a node selector (e.g., `::comment`).
 */
// Class renamed from QueryParserTestTest17 for clarity.
public class QueryParserTest {

    @Test
    void parsesHasWithNodeSelector() {
        // This test verifies that a query to find an element `:has()` a specific
        // node type (e.g., a comment) with a certain value is parsed and executed correctly.

        // ARRANGE
        // The query should find <p> elements that contain a comment node with the text "some text".
        String query = "p:has(::comment:contains(some text))";
        String html = "<div>" +
                      "  <p id='p1'>Matches. <!-- some text --></p>" +         // Should be selected
                      "  <p id='p2'>No match. <!-- other text --></p>" +      // Should NOT be selected
                      "  <p id='p3'>No comment.</p>" +                         // Should NOT be selected
                      "  <div>Has comment, but not a p. <!-- some text --></div>" + // Should NOT be selected
                      "</div>";
        Document doc = Jsoup.parse(html);

        // ACT
        Elements selectedElements = doc.select(query);

        // ASSERT
        // 1. Functional assertion: Verify the selector works as intended on sample HTML.
        // This is more robust and understandable than checking the parser's internal state.
        assertEquals(1, selectedElements.size(), "Should select exactly one element.");
        assertEquals("p1", selectedElements.first().id(), "The selected element should be the one with id 'p1'.");

        // 2. Parser fidelity assertion: Verify the parser can reconstruct the original query.
        Evaluator parsedEvaluator = QueryParser.parse(query);
        assertEquals(query, parsedEvaluator.toString(), "The parsed evaluator's string representation should match the original query.");
    }
}