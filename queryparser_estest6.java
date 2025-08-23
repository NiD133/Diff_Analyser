package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the static utility methods in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that QueryParser.and() returns null when both input Evaluators are null.
     * This tests the edge case where the method is called with no valid evaluators to combine.
     */
    @Test
    public void andShouldReturnNullWhenBothEvaluatorsAreNull() {
        // Arrange: No setup is necessary as we are testing with null literals.

        // Act: Call the 'and' method with two null arguments.
        Evaluator result = QueryParser.and(null, null);

        // Assert: The resulting evaluator should be null.
        assertNull("Combining two null evaluators should result in null.", result);
    }
}