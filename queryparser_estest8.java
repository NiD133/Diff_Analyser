package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the static QueryParser.and() method.
 * This test focuses on its handling of null inputs.
 */
public class QueryParserAndMethodTest {

    /**
     * Verifies that when the right-hand evaluator is null,
     * the QueryParser.and() method correctly returns the left-hand evaluator.
     */
    @Test
    public void andShouldReturnLeftEvaluatorWhenRightIsNull() {
        // Arrange: Create a non-null evaluator for the left-hand side.
        // The original test used a StructuralEvaluator.Not containing a null,
        // which is a valid, non-null Evaluator instance. We'll use a simpler one
        // for clarity, but the principle is the same.
        Evaluator leftEvaluator = new Evaluator.Tag("div");
        Evaluator rightEvaluator = null;

        // Act: Call the method under test.
        Evaluator result = QueryParser.and(leftEvaluator, rightEvaluator);

        // Assert: The result should be the exact same instance as the left evaluator.
        // The method should not throw an exception, contrary to the original test's expectation.
        assertSame("Expected the left evaluator to be returned when the right is null",
            leftEvaluator, result);
    }
}