package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TransformedBag#getBag()}.
 * This particular test focuses on the subclass {@link TransformedSortedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that getBag() returns the original, decorated bag instance.
     */
    @Test
    public void testGetBagReturnsDecoratedBagInstance() {
        // Arrange
        // 1. Create an empty source bag that will be decorated.
        final SortedBag<String> sourceBag = new TreeBag<>();

        // 2. The specific transformer is not important for this test, as we are not adding elements.
        final Transformer<String, String> transformer = ConstantTransformer.constantTransformer("anyValue");

        // 3. Create the transformed bag, which decorates the source bag.
        final TransformedSortedBag<String> transformedBag =
                TransformedSortedBag.transformingSortedBag(sourceBag, transformer);

        // Act
        // Retrieve the underlying bag using the getBag() method.
        final Bag<String> decoratedBag = transformedBag.getBag();

        // Assert
        // The returned bag should be the exact same instance as the original source bag.
        assertSame("getBag() should return the same instance that was decorated", sourceBag, decoratedBag);

        // As a secondary check, verify the bag is still in its original state (empty).
        assertTrue("The decorated bag should be empty", decoratedBag.isEmpty());
    }
}