package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Contains tests for the static factory methods of {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that {@link TransformedBag#transformedBag(Bag, Transformer)} throws
     * an UnsupportedOperationException when given an unmodifiable bag.
     * <p>
     * This factory method attempts to transform the elements already present in the
     * source bag, which involves modification and is not allowed on an unmodifiable bag.
     */
    @Test
    public void transformedBagWithUnmodifiableBagShouldThrowException() {
        // Arrange: Create a non-empty, unmodifiable bag.
        final Bag<Integer> sourceBag = new HashBag<>();
        sourceBag.add(1); // The bag must be non-empty to trigger the transformation attempt.

        final Bag<Integer> unmodifiableBag = UnmodifiableBag.unmodifiableBag(sourceBag);
        final Transformer<Integer, Integer> transformer = ConstantTransformer.constantTransformer(null);

        // Act & Assert: Attempting to create a transformed bag should fail.
        try {
            TransformedBag.transformedBag(unmodifiableBag, transformer);
            fail("Expected an UnsupportedOperationException because the decorated bag is unmodifiable.");
        } catch (final UnsupportedOperationException e) {
            // This is the expected behavior. The factory method correctly fails
            // when it cannot modify the source bag to transform its elements.
        }
    }
}