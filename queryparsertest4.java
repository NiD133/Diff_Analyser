package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the parsing logic of {@link QueryParser}, specifically verifying its
 * S-expression debug output for complex queries.
 */
public class QueryParserTest {

    @Test
    @DisplayName("Should parse a query with multiple, comma-separated selectors into a corresponding 'Or' structure")
    void parsesMultiSelectorQueryIntoCorrectStructure() {
        // ARRANGE
        // A complex query with two selectors joined by the 'or' (,) combinator:
        // 1: .foo.qux[attr=bar] > ol.bar
        // 2: ol > li + li
        String query = ".foo.qux[attr=bar] > ol.bar, ol > li + li";

        // The expected S-expression represents the parsed evaluator tree.
        // The parser creates a top-level 'Or' evaluator for the comma.
        // Note: The parser processes selectors from right to left, so the second
        // selector ("ol > li + li") appears first in the S-expression.
        String expectedSExpression =
            "(Or " +
                // Corresponds to "ol > li + li"
                "(And " +
                    "(Tag 'li')" +
                    "(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li')))" +
                ")" +
                // Corresponds to ".foo.qux[attr=bar] > ol.bar"
                "(ImmediateParentRun " +
                    "(And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))" +
                    "(And (Tag 'ol')(Class '.bar'))" +
                ")" +
            ")";

        // ACT
        String actualSExpression = sexpr(query);

        // ASSERT
        assertEquals(expectedSExpression, actualSExpression);
    }
}