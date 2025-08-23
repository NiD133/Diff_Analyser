package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link QueryParser}.
 * This class focuses on parsing complex CSS selectors.
 */
public class QueryParserTest {

    @Test
    @DisplayName("should parse a complex query with multiple structural combinators and pseudo-classes")
    void parsesComplexStructuralQuery() {
        // Arrange
        // A complex query with descendant (' '), child ('>'), adjacent sibling ('+'),
        // and general sibling ('~') combinators, plus nested pseudo-classes.
        String query = "a:not(:has(span.foo)) b d > e + f ~ g";

        /*
         The expected S-expression (Symbolic Expression) represents the parsed query's internal structure.
         Jsoup's parser evaluates selectors from right to left, leading to a deeply nested structure.
         The query `a:not(:has(span.foo)) b d > e + f ~ g` is parsed as:
         - A 'g' tag...
         - ...that is a general sibling of an 'f' tag...
         - ...that is an adjacent sibling of an 'e' tag...
         - ...that is a direct child of a 'd' tag...
         - ...that is a descendant of a 'b' tag...
         - ...that is a descendant of an 'a' tag that does not have a descendant 'span.foo'.

         Formatted for readability, the S-expression looks like this:
         (And
           (Tag 'g')
           (PreviousSibling (And
             (Tag 'f')
             (ImmediatePreviousSibling (ImmediateParentRun
               (And
                 (Tag 'd')
                 (Ancestor (And
                   (Tag 'b')
                   (Ancestor (And
                     (Tag 'a')
                     (Not (Has (And (Tag 'span') (Class '.foo')))))
                   ))
                 ))
               )
               (Tag 'e')
             ))
           ))
         )
        */
        String expectedSExpression = "(And (Tag 'g')(PreviousSibling (And (Tag 'f')(ImmediatePreviousSibling (ImmediateParentRun (And (Tag 'd')(Ancestor (And (Tag 'b')(Ancestor (And (Tag 'a')(Not (Has (And (Tag 'span')(Class '.foo')))))))))(Tag 'e'))))))";

        // Act
        Evaluator evaluator = QueryParser.parse(query);
        // sexpr is a debug helper that parses and returns the S-expression
        String actualSExpression = sexpr(query);

        // Assert
        // 1. Verify that the evaluator's toString() method reconstructs the original query.
        assertEquals(query, evaluator.toString());

        // 2. Verify that the internal parsed structure matches the expected S-expression.
        assertEquals(expectedSExpression, actualSExpression);
    }
}