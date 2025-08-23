package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.junit.Test;

/**
 * This test class contains a test case for TransformedBag functionality,
 * specifically focusing on interactions with a sorted underlying bag.
 */
public class TransformedBagRefactoredTest {

    /**
     * Tests that a ClassCastException is thrown when attempting to add a
     * non-Comparable object to a TransformedBag that decorates a TreeBag.
     * <p>
     * The exception originates from the underlying TreeBag, which requires
     * elements to be comparable for sorting. This test verifies that the `add`
     * call is correctly delegated to the decorated bag.
     */
    @Test(timeout = 4000, expected = ClassCastException.class)
    public void addNonComparableToTransformedSortedBagShouldThrowClassCastException() {
        // Arrange
        // 1. A TreeBag is a SortedBag that requires its elements to be Comparable.
        final SortedBag<Object> underlyingSortedBag = new TreeBag<>();

        // 2. Use an identity transformer that passes objects through unchanged.
        //    An empty ChainedTransformer conveniently acts as an identity transformer.
        final Transformer<Object, Object> identityTransformer =
                new ChainedTransformer<>(new Transformer[0]);

        // 3. The bag under test, which decorates the TreeBag.
        final Bag<Object> transformedBag =
                TransformedSortedBag.transformingSortedBag(underlyingSortedBag, identityTransformer);

        // 4. An instance of Object does not implement Comparable.
        final Object nonComparableObject = new Object();

        // Act
        // Attempt to add the non-Comparable object. The identity transformer will
        // pass it directly to the underlying TreeBag. The TreeBag will then throw
        // a ClassCastException because it cannot sort the new element.
        transformedBag.add(nonComparableObject, 477);

        // Assert
        // The test passes if a ClassCastException is thrown, as specified by the
        // @Test(expected=...) annotation.
    }
}