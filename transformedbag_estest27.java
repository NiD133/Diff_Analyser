package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Tests for {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that adding an element to a TransformedBag throws a NullPointerException
     * if the transformer converts the element to null and the underlying decorated bag
     * does not support null elements.
     */
    @Test(expected = NullPointerException.class)
    public void testAddTransformedNullToBagThatDisallowsNulls() {
        // Arrange
        // 1. The decorated bag (TreeBag) does not allow null elements.
        final Bag<Integer> underlyingBag = new TreeBag<>();

        // 2. The transformer converts any input element to null.
        final Transformer<Integer, Integer> nullTransformer = ConstantTransformer.nullTransformer();

        // 3. The TransformedBag uses the null-disallowing bag and the null-transformer.
        final Bag<Integer> transformedBag = TransformedBag.transformingBag(underlyingBag, nullTransformer);

        // Act & Assert
        // Attempting to add an element will first transform it to null.
        // The subsequent call to the underlying TreeBag's add(null, count) will
        // throw a NullPointerException.
        transformedBag.add(414, 10);
    }
}