package org.jsoup.select;

import org.junit.Test;

/**
 * Test suite for the static methods in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that calling the combinator method with the '+' (adjacent sibling)
     * operator and null Evaluator arguments throws a NullPointerException.
     * This ensures the method correctly handles invalid null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void combinatorWithAdjacentSiblingThrowsNPEForNullEvaluators() {
        // The '+' combinator requires non-null evaluators for the left and right-hand sides.
        // This call is expected to fail with a NullPointerException.
        QueryParser.combinator(null, '+', null);
    }
}