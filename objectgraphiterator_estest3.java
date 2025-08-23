package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.junit.Test;

/**
 * Test class for {@link ObjectGraphIterator}.
 */
public class ObjectGraphIterator_ESTestTest3 {

    /**
     * Tests that an exception thrown by a nested iterator's transformer
     * is correctly propagated by the outer iterator.
     */
    @Test(expected = NullPointerException.class)
    public void updateCurrentIteratorWithFailingNestedIteratorShouldPropagateNPE() {
        // ARRANGE
        // 1. Define a root element for an inner iterator.
        final Integer innerIteratorRoot = 1;

        // 2. Create a transformer that is designed to fail.
        // The predicate will check if the input is a Boolean. Since our root is an Integer,
        // the predicate will return false.
        final Predicate<Object> isBooleanPredicate = InstanceofPredicate.instanceOfPredicate(Boolean.class);

        // The IfTransformer is constructed with null for both its "true" and "false"
        // delegate transformers. When it attempts to use the "false" transformer,
        // it will throw a NullPointerException.
        final Transformer<Object, Integer> failingTransformer =
                new IfTransformer<>(isBooleanPredicate, null, null);

        // 3. Create an inner iterator that uses the failing transformer.
        final ObjectGraphIterator<Integer> innerIterator =
                new ObjectGraphIterator<>(innerIteratorRoot, failingTransformer);

        // 4. Create an outer iterator that wraps the inner one.
        final ObjectGraphIterator<Integer> outerIterator =
                new ObjectGraphIterator<>(innerIterator);

        // ACT
        // This call will trigger traversal of the inner iterator, which in turn will
        // use the failing transformer, causing a NullPointerException to be thrown.
        // The @Test(expected) annotation verifies that this exception is thrown as expected.
        outerIterator.updateCurrentIterator();
    }
}