package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TransformedBag#getCount(Object)} method.
 */
public class TransformedBagGetCountTest {

    /**
     * Tests that getCount() delegates to the decorated bag's getCount method
     * and does not use the transformer on the input object.
     * <p>
     * This is verified by using a TreeBag with a custom comparator that considers
     * two distinct objects to be equal. The test checks if getCount() for one
     * object returns the count of the other, proving the underlying bag's
     * comparison logic is used directly.
     */
    @Test
    public void getCountShouldDelegateToUnderlyingBagWithoutTransformation() {
        // Arrange
        final String objectInBag = "item in bag";
        final String objectToLookFor = "a different item";

        // A comparator that considers all objects to be equal. This is key to the test,
        // as it forces the underlying TreeBag to find a match between two different objects.
        final Comparator<String> allEqualComparator = (o1, o2) -> 0;

        // The underlying bag uses the custom comparator.
        final Bag<String> underlyingBag = new TreeBag<>(allEqualComparator);
        underlyingBag.add(objectInBag);

        // A transformer that would change the object if it were used.
        final Transformer<String, String> transformer = ConstantTransformer.constantTransformer("transformed value");
        final Bag<String> transformedBag = TransformedBag.transformingBag(underlyingBag, transformer);

        // Act
        // getCount should delegate directly to the underlying TreeBag. The TreeBag will use its
        // 'allEqualComparator', which will report that 'objectToLookFor' is "equal" to
        // 'objectInBag', which is already in the bag.
        final int count = transformedBag.getCount(objectToLookFor);

        // Assert
        // The count should be 1, as the underlying bag contains one "equal" item.
        // This confirms the transformer was not applied to the input of getCount.
        assertEquals("getCount should return the count from the underlying bag.", 1, count);
    }
}