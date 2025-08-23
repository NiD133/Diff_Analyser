package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Contains tests for the {@link TransformedBag} class and its subclasses,
 * focusing on method behavior with incompatible types.
 */
public class TransformedBag_ESTestTest22 {

    /**
     * Tests that getCount() on a TransformedSortedBag throws a ClassCastException
     * when the decorated bag is a TreeBag and the object provided is not
     * comparable to the bag's elements.
     * <p>
     * The getCount() method passes its argument directly to the decorated bag
     * without transformation. The underlying TreeBag, which requires comparable
     * elements, throws a ClassCastException because it cannot compare the
     * incompatible object type.
     */
    @Test(expected = ClassCastException.class)
    public void getCountWithIncompatibleTypeOnDecoratedTreeBagThrowsClassCastException() {
        // Arrange
        // A TreeBag is a SortedBag that requires its elements to be Comparable.
        final SortedBag<Object> underlyingTreeBag = new TreeBag<>();

        // The transformer is required for instantiation but is not invoked by getCount().
        // Using a simple transformer makes it clear it's not part of the test's logic.
        final Transformer<Object, Object> dummyTransformer = ConstantTransformer.nullTransformer();

        // The TransformedSortedBag decorates the TreeBag.
        final SortedBag<Object> transformedBag =
                TransformedSortedBag.transformingSortedBag(underlyingTreeBag, dummyTransformer);

        // An object that is not Comparable, which the underlying TreeBag cannot handle.
        final Object incompatibleObject = new Object();

        // Act & Assert
        // This call is delegated to the underlying TreeBag, which throws a
        // ClassCastException when it tries to compare the incompatible object.
        transformedBag.getCount(incompatibleObject);
    }
}