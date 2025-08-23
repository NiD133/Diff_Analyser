package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for CSS query parsing logic in {@link QueryParser}, focusing on operator precedence.
 */
public class QueryParserTest {

    /**
     * Verifies that the comma combinator (logical OR) has the correct, lower precedence
     * compared to the descendant combinator (logical AND).
     * A query like "a b, c d" should be parsed as (a AND b) OR (c AND d).
     */
    @Test
    @DisplayName("OR combinator (,) should have lower precedence than descendant combinator (space)")
    void orHasLowerPrecedenceThanDescendant() {
        // Arrange: A query with three descendant selectors joined by OR combinators.
        String query = "a b, c d, e f";

        // Act: Parse the query into an S-expression for structural validation.
        String actualExpression = sexpr(query);

        // Assert: The resulting structure should be a top-level OR containing three ANDs.
        // The S-expression format makes the parsed structure explicit:
        // (Or
        //   (And (Tag 'b') (Ancestor (Tag 'a')))
        //   (And (Tag 'd') (Ancestor (Tag 'c')))
        //   (And (Tag 'f') (Ancestor (Tag 'e')))
        // )
        String expectedExpression =
            "(Or" +
                "(And (Tag 'b')(Ancestor (Tag 'a')))" +
                "(And (Tag 'd')(Ancestor (Tag 'c')))" +
                "(And (Tag 'f')(Ancestor (Tag 'e')))" +
            ")";

        assertEquals(expectedExpression, actualExpression);
    }
}