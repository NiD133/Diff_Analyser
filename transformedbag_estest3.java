package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that calling remove() with a count of zero does not modify the bag
     * and returns false, as specified by the Bag interface contract.
     */
    @Test
    public void removeWithZeroCopiesShouldReturnFalseAndNotModifyBag() {
        // Arrange
        final Bag<Integer> underlyingBag = new HashBag<>();
        final Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        final Bag<Integer> transformedBag = new TransformedBag<>(underlyingBag, transformer);

        final String objectToRemove = "any object";
        final int copiesToRemove = 0;

        // Act
        final boolean wasModified = transformedBag.remove(objectToRemove, copiesToRemove);

        // Assert
        assertFalse("Removing zero copies of an element should return false.", wasModified);
        assertTrue("The bag should not be modified when removing zero copies.", transformedBag.isEmpty());
    }
}