package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the QueryParser's static helper methods.
 * This class focuses on the combinator logic.
 */
public class QueryParserCombinatorTest {

    /**
     * Verifies that the `combinator` method, when used with a space character,
     * creates a new structural evaluator representing a descendant relationship.
     * A CSS selector like "div .content" uses a space as a descendant combinator.
     * This test ensures that the method correctly wraps the evaluators rather than
     * returning one of the original instances.
     */
    @Test
    public void combinatorWithSpaceShouldCreateNewDescendantEvaluator() {
        // Arrange: Create two distinct evaluators representing an ancestor and a descendant.
        // e.g., for a selector like "div.container p"
        Evaluator ancestorEvaluator = new Evaluator.Tag("div");
        Evaluator descendantEvaluator = new Evaluator.Tag("p");
        char descendantCombinator = ' ';

        // Act: Combine the two evaluators using the descendant combinator.
        Evaluator combinedEvaluator = QueryParser.combinator(ancestorEvaluator, descendantCombinator, descendantEvaluator);

        // Assert: The result must be a new evaluator instance, not the same as the inputs.
        // This confirms that a new structural evaluator was created to represent the relationship.
        assertNotNull(combinedEvaluator);
        assertNotSame(
            "The combinator method should return a new wrapper evaluator, not the original descendant evaluator.",
            descendantEvaluator,
            combinedEvaluator
        );
        assertNotSame(
            "The combinator method should return a new wrapper evaluator, not the original ancestor evaluator.",
            ancestorEvaluator,
            combinedEvaluator
        );
    }
}