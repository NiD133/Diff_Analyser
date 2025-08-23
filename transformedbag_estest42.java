package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TransformedBag#getCount(Object)} method.
 */
public class TransformedBagGetCountTest {

    /**
     * Tests that getCount() on an empty TransformedBag always returns 0,
     * regardless of the object being checked.
     */
    @Test
    public void getCountOnEmptyBagShouldReturnZero() {
        // Arrange
        final Bag<Integer> emptyDecoratedBag = new HashBag<>();
        
        // A transformer is required by the constructor, but its behavior is irrelevant
        // for this test since the bag is empty. We use a simple one.
        final Transformer<Object, Integer> transformer = ConstantTransformer.constantTransformer(42);
        
        final Bag<Integer> transformedBag = TransformedBag.transformingBag(emptyDecoratedBag, transformer);

        final Object objectToCount = new Object();

        // Act
        final int count = transformedBag.getCount(objectToCount);

        // Assert
        assertEquals("The count of any object in an empty bag should be 0", 0, count);
    }
}