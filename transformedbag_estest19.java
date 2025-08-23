package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

// Note: The original test class name and inheritance structure are preserved.
// Unnecessary imports from the original test have been removed for clarity.
public class TransformedBag_ESTestTest19 extends TransformedBag_ESTest_scaffolding {

    /**
     * Tests that calling getCount(null) on a TransformedSortedBag that wraps a TreeBag
     * throws a NullPointerException.
     * <p>
     * This behavior is expected because the underlying TreeBag does not support null
     * elements, and the decorator should propagate this exception.
     */
    @Test(expected = NullPointerException.class)
    public void getCountWithNullShouldThrowNPEWhenDecoratingTreeBag() {
        // Arrange
        // A TreeBag is a SortedBag that does not permit null elements.
        final SortedBag<Object> underlyingBag = new TreeBag<>();

        // A transformer is required to create the decorated bag, but its specific
        // implementation is not relevant for the getCount() method being tested.
        final Transformer<Object, Object> dummyTransformer = ConstantTransformer.nullTransformer();

        final SortedBag<Object> transformedBag =
                TransformedSortedBag.transformedSortedBag(underlyingBag, dummyTransformer);

        // Act & Assert
        // This call is expected to throw a NullPointerException because it is
        // delegated to the underlying TreeBag, which cannot handle null.
        transformedBag.getCount(null);
    }
}