package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Tests for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    @Test
    public void addWithCount_whenDecoratedBagThrowsException_propagatesException() {
        // Arrange
        // 1. Create an underlying TreeBag. A TreeBag is a SortedBag that does not
        //    permit null elements and will throw a NullPointerException if one is added.
        final SortedBag<Object> underlyingBag = new TreeBag<>();

        // 2. Create a transformer that will convert any element into null.
        final Transformer<Object, Object> nullTransformer = ConstantTransformer.nullTransformer();

        // 3. Decorate the TreeBag with a TransformedSortedBag. When an element is added
        //    to this bag, it will be transformed to null before being passed to the underlying TreeBag.
        final SortedBag<Object> transformedBag =
                TransformedSortedBag.transformingSortedBag(underlyingBag, nullTransformer);

        // 4. The CollectionSortedBag under test, which decorates the transformed bag.
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(transformedBag);

        final Object elementToAdd = "any non-null object";
        final int count = 5;

        // Act & Assert
        // The add operation should fail because the nullTransformer will produce a null element,
        // which the underlying TreeBag rejects. We expect a NullPointerException to be propagated.
        try {
            collectionSortedBag.add(elementToAdd, count);
            fail("Expected a NullPointerException to be thrown.");
        } catch (final NullPointerException e) {
            // Success: The expected exception was caught.
            // The exception originates from the underlying TreeBag's inability to handle nulls.
        }
    }
}