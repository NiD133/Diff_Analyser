package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the parsing of CSS OR combinators (the comma character), particularly in cases
 * where the combinator follows an attribute selector like `[attr=val]`.
 * <p>
 * These tests verify that the parser correctly handles the comma as a separator
 * for a new selector, rather than misinterpreting it as part of the preceding selector.
 * The `sexpr` helper function is used to get a string representation (S-expression)
 * of the parsed evaluator tree for easy structural comparison.
 *
 * @see <a href="https://github.com/jhy/jsoup/issues/2073">GitHub Issue #2073</a>
 */
@DisplayName("QueryParser: OR Combinator Parsing")
class QueryParserOrCombinatorTest {

    @Test
    @DisplayName("should parse an OR combinator after a descendant attribute selector")
    void parsesOrAfterDescendantAttributeSelector() {
        // Arrange: A query where an OR combinator follows a descendant attribute selector.
        String query = "#parent [class*=child], .some-other-selector";

        // The expected S-expression represents: (OR (.some-other-selector) (AND ([class*=child]) (Ancestor #parent)))
        String expectedSExpr = "(Or (Class '.some-other-selector')(And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent'))))";

        // Act
        String actualSExpr = sexpr(query);

        // Assert
        assertEquals(expectedSExpr, actualSExpr);
    }

    @Test
    @DisplayName("should parse an OR combinator after an attribute selector attached to an element")
    void parsesOrAfterAttachedAttributeSelector() {
        // Arrange: A query where an OR combinator follows an attribute selector directly attached to an ID.
        String query = "#el[class*=child], .some-other-selector";

        // The expected S-expression represents: (OR (AND (#el) ([class*=child])) (.some-other-selector))
        String expectedSExpr = "(Or (And (Id '#el')(AttributeWithValueContaining '[class*=child]'))(Class '.some-other-selector'))";

        // Act
        String actualSExpr = sexpr(query);

        // Assert
        assertEquals(expectedSExpr, actualSExpr);
    }

    @Test
    @DisplayName("should parse an OR combinator when both sides of the combinator are complex selectors")
    void parsesOrWithComplexSelectorsOnBothSides() {
        // Arrange: A query where both parts of the OR are compound selectors.
        String query = "#parent [class*=child], .some-other-selector .nested";

        // The expected S-expression represents: (OR (AND ([class*=child]) (Ancestor #parent)) (AND (.nested) (Ancestor .some-other-selector)))
        String expectedSExpr = "(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))";

        // Act
        String actualSExpr = sexpr(query);

        // Assert
        assertEquals(expectedSExpr, actualSExpr);
    }
}