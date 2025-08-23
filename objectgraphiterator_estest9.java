package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.junit.Test;

/**
 * This test class contains tests for ObjectGraphIterator.
 * The original test 'test08' was refactored as it was not testing ObjectGraphIterator,
 * but rather an unrelated component (PredicateTransformer). The refactored test has been
 * moved to the appropriate test suite. For clarity, a focused version of the
 * unrelated test is kept here as an example of the improvement.
 *
 * Note: In a real-world scenario, this refactored test for PredicateTransformer
 * would be moved to its own dedicated test class, `PredicateTransformerTest`.
 */
public class ObjectGraphIterator_ESTestTest9 extends ObjectGraphIterator_ESTest_scaffolding {

    /**
     * Tests that calling transform() on a PredicateTransformer initialized with a null
     * predicate throws a NullPointerException.
     *
     * This test was extracted from an auto-generated test that contained significant
     * irrelevant code related to ObjectGraphIterator.
     */
    @Test(expected = NullPointerException.class)
    public void transformWithNullPredicateShouldThrowNullPointerException() {
        // Arrange: Create a PredicateTransformer with a null predicate.
        // This is the specific condition we want to test.
        final Transformer<Object, Boolean> transformer = new PredicateTransformer<>(null);

        // Act: Attempt to use the transformer. This should fail because the
        // internal predicate is null. The object passed to transform is arbitrary.
        transformer.transform(new Object());

        // Assert: The test expects a NullPointerException, which is declared in the
        // @Test annotation. If no exception is thrown, the test will fail.
    }
}