package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Contains tests for the {@link TransformedBag} class, focusing on its behavior
 * when interacting with underlying collections that have type constraints.
 */
public class TransformedBagTest {

    /**
     * Tests that calling remove() with an object of an incompatible type throws a
     * ClassCastException.
     * <p>
     * The exception is not thrown by TransformedBag itself, but is propagated from
     * the decorated TreeBag, which cannot compare the incompatible object type.
     */
    @Test(expected = ClassCastException.class)
    public void testRemoveWithIncompatibleTypeThrowsClassCastException() {
        // Arrange
        final SortedBag<Integer> underlyingBag = new TreeBag<>();

        // The transformer is required by the factory method, but its specific
        // behavior is irrelevant here because the remove() method does not transform
        // the object being removed.
        final Transformer<Integer, Integer> identityTransformer = ConstantTransformer.nullTransformer();
        final Bag<Integer> transformedBag =
                TransformedSortedBag.transformingSortedBag(underlyingBag, identityTransformer);

        final Object objectOfIncompatibleType = "This is not an Integer";

        // Act
        // The remove operation is delegated to the underlying TreeBag. The TreeBag
        // will attempt to compare the incompatible object ("a string") with its
        // elements (Integers), which results in a ClassCastException.
        transformedBag.remove(objectOfIncompatibleType, 1);

        // Assert: The test will pass if a ClassCastException is thrown.
    }
}