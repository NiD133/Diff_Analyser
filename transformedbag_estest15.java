package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Tests the behavior of a {@link TransformedBag} when it decorates an unmodifiable bag.
 */
public class TransformedBagUnmodifiableTest {

    /**
     * Verifies that calling remove() on a TransformedBag wrapping an UnmodifiableBag
     * correctly throws an UnsupportedOperationException.
     * <p>
     * The modification methods on TransformedBag should delegate to the underlying
     * collection. If that collection is unmodifiable, the operation must fail.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeOnTransformedUnmodifiableBagShouldThrowException() {
        // Arrange: Create a TransformedBag that decorates an unmodifiable bag.
        final SortedBag<Object> unmodifiableBag =
                UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());

        // The specific transformer is irrelevant, as the remove() operation does not
        // transform elements. A "no-op" transformer makes this intent clear.
        final Transformer<Object, Object> nopTransformer = NOPTransformer.nopTransformer();
        final Bag<Object> transformedBag = TransformedBag.transformingBag(unmodifiableBag, nopTransformer);

        // Act & Assert: Attempting to remove an element must throw an exception
        // because the operation is delegated to the unmodifiable decorated bag.
        // The object and count being removed are arbitrary.
        transformedBag.remove("any object", 1);
    }
}