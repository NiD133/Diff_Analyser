package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Tests for {@link TransformedBag} focusing on exception propagation.
 */
public class TransformedBagTest {

    /**
     * Tests that getCount() propagates a NullPointerException from the decorated bag
     * when the underlying collection does not support null elements.
     */
    @Test(expected = NullPointerException.class)
    public void testGetCountWithNullShouldPropagateExceptionFromDecoratedBag() {
        // Arrange
        // A TreeBag is used as the decorated bag because it does not permit null elements.
        final SortedBag<Object> decorated = new TreeBag<>();

        // The specific transformer is irrelevant for getCount(), so a simple one is used.
        final Transformer<Object, Object> transformer = ConstantTransformer.constantTransformer("ignored");

        final Bag<Object> transformedBag = TransformedBag.transformingBag(decorated, transformer);

        // Act & Assert
        // Calling getCount(null) should throw a NullPointerException because the
        // underlying TreeBag throws it, and TransformedBag must propagate it.
        transformedBag.getCount(null);
    }
}