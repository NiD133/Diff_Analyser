package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.junit.Test;

/**
 * Tests for {@link TransformedBag} focusing on interactions with sorted bags.
 */
public class TransformedBagTest {

    /**
     * Tests that adding an element to a TransformedBag throws a ClassCastException
     * if the underlying bag is sorted (like TreeBag) and the transformer produces
     * a non-comparable object.
     */
    @Test(expected = ClassCastException.class)
    public void addShouldThrowExceptionWhenTransformedObjectIsNotComparableForSortedBag() {
        // Arrange
        // A TreeBag is a SortedBag, which requires its elements to be Comparable.
        final Bag<Object> underlyingSortedBag = new TreeBag<>();

        // This transformer converts any input into a new Object(), which is not Comparable.
        final Transformer<Object, Object> nonComparableObjectTransformer = input -> new Object();

        final Bag<Object> transformedBag = new TransformedBag<>(underlyingSortedBag, nonComparableObjectTransformer);

        // Act & Assert
        // When we add an element, it gets transformed into a non-Comparable Object.
        // The underlying TreeBag then throws a ClassCastException because it cannot
        // sort an object that does not implement the Comparable interface.
        // The input object ("any object") and count (5) are arbitrary.
        transformedBag.add("any object", 5);
    }
}