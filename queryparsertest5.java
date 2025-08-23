package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link QueryParser}, focusing on the structure of the parsed evaluators.
 */
class QueryParserTest {

    /**
     * Verifies that the parser creates an optimized evaluator structure for a query
     * combining an ID, a descendant combinator, and a class.
     * <p>
     * For a query like {@code "#id .class"}, it is more performant to first find all elements
     * with the class {@code .class} and then check if they have an ancestor with the ID {@code #id}.
     * This is more efficient than finding the {@code #id} element and then scanning all of its descendants.
     * This test ensures the parser generates this optimized structure.
     *
     * @see <a href="https://github.com/jhy/jsoup/issues/2254">GitHub Issue #2254</a>
     */
    @Test
    @DisplayName("should parse '#id .class' into an optimized structure")
    void parserCreatesOptimizedStructureForIdDescendantClassSelector() {
        // Arrange
        String query = "#id .class";

        // The expected S-expression represents the parsed evaluator tree.
        // This structure signifies an optimized evaluation order:
        // 1. (Class '.class'): First, find elements with the class 'class'.
        // 2. (Ancestor (Id '#id')): Then, from that set, find elements that have an ancestor with the ID 'id'.
        // 3. (And ...): Both conditions must be met.
        String expectedStructure = "(And (Class '.class')(Ancestor (Id '#id')))";

        // Act
        // The sexpr() helper function parses the query and returns its S-expression representation.
        String actualStructure = sexpr(query);

        // Assert
        assertEquals(expectedStructure, actualStructure, "The parsed structure should be optimized for performance.");
    }
}