package org.jsoup.select;

import org.junit.Test;

/**
 * Tests for the static helper methods in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Tests that the {@code QueryParser.or()} method throws a NullPointerException
     * when its arguments are null. This is because the underlying combining evaluator
     * requires non-null evaluators.
     */
    @Test(expected = NullPointerException.class)
    public void orThrowsNullPointerExceptionForNullEvaluators() {
        QueryParser.or(null, null);
    }
}