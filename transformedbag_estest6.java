package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link TransformedBag#getBag()} method.
 */
public class TransformedBag_ESTestTest6 {

    /**
     * Tests that getBag() returns the original, underlying bag that was decorated.
     */
    @Test
    public void getBag_shouldReturnTheUnderlyingDecoratedBag() {
        // Arrange
        final Bag<Object> underlyingBag = new HashBag<>();
        final Integer originalElement = 165;
        underlyingBag.add(originalElement);

        // A transformer is required for the constructor, but its behavior is not relevant for this test.
        final Transformer<Object, Object> transformer = ConstantTransformer.constantTransformer(null);
        final TransformedBag<Object> transformedBag = new TransformedBag<>(underlyingBag, transformer);

        // Act
        final Bag<Object> retrievedBag = transformedBag.getBag();

        // Assert
        // 1. Verify that getBag() returns the exact same instance as the original bag.
        assertSame("getBag() should return the same instance of the decorated bag", underlyingBag, retrievedBag);

        // 2. Verify the returned bag is unmodified and still contains the original element.
        assertTrue("The returned bag should contain the original element", retrievedBag.contains(originalElement));
        assertEquals("The returned bag should have the correct count for the element", 1, retrievedBag.getCount(originalElement));
    }
}