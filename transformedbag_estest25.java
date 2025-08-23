package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Contains tests for {@link TransformedBag} to ensure it correctly handles
 * operations when decorating an unmodifiable bag.
 */
public class TransformedBag_ESTestTest25 {

    /**
     * Verifies that attempting to add an element to a TransformedBag that decorates
     * an UnmodifiableBag correctly throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addShouldThrowExceptionWhenDecoratingUnmodifiableBag() {
        // Arrange: Create a TransformedBag that wraps an unmodifiable bag.
        final Bag<Integer> unmodifiableBag = UnmodifiableBag.unmodifiableBag(new HashBag<>());
        final Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        final Bag<Integer> transformedBag = new TransformedBag<>(unmodifiableBag, transformer);

        // Act & Assert: Attempting to add an element should throw an exception
        // because the underlying bag does not support modification.
        transformedBag.add(100, 5);
    }
}