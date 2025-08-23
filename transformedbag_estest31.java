package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.junit.Test;

/**
 * This test verifies the behavior of TransformedBag when using a misconfigured
 * SwitchTransformer.
 */
public class TransformedBagTest {

    /**
     * Tests that adding an element to a TransformedBag throws an ArrayIndexOutOfBoundsException
     * if the decorating SwitchTransformer is created with mismatched predicate and transformer arrays.
     *
     * The SwitchTransformer is configured with two predicates but zero transformers. When an
     * element is added, the transformer is invoked. The second predicate evaluates to true,
     * causing the SwitchTransformer to attempt to access a transformer at index 1 in an
     * empty array, which triggers the expected exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void addWithCountShouldThrowExceptionForMismatchedSwitchTransformer() {
        // Arrange
        // 1. Create a SwitchTransformer with more predicates (2) than transformers (0).
        final Predicate<Object> alwaysFalse = FalsePredicate.falsePredicate();
        final Predicate<Object> alwaysTrue = new NotPredicate<>(alwaysFalse);

        final Predicate<? super Integer>[] predicates = new Predicate[]{alwaysFalse, alwaysTrue};
        final Transformer<? super Integer, ? extends Integer>[] transformers = new Transformer[0]; // Empty array

        // This transformer is configured to fail when the second predicate is met.
        final Transformer<Integer, Integer> faultySwitchTransformer =
            new SwitchTransformer<>(predicates, transformers, ExceptionTransformer.exceptionTransformer());

        // 2. Decorate a bag with the faulty transformer.
        // The specific bag implementation (e.g., TreeBag) is not critical here.
        final SortedBag<Integer> underlyingBag = new TreeBag<>();
        final Bag<Integer> transformedBag =
            TransformedSortedBag.transformingSortedBag(underlyingBag, faultySwitchTransformer);

        // Act
        // Attempt to add an element, which will trigger the faulty transformer.
        transformedBag.add(123, 5);

        // Assert
        // The @Test(expected=...) annotation asserts that an ArrayIndexOutOfBoundsException is thrown.
    }
}