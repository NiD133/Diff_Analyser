package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertFalse;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Contains tests for the {@link TransformedBag#equals(Object)} method.
 */
public class TransformedBag_ESTestTest34 { // The original class name is kept for context.

    /**
     * Tests that a TransformedBag is not equal to an object of a completely different type.
     * The equals method should return false without throwing an exception.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithObjectOfDifferentType() {
        // Arrange
        final Bag<Object> decoratedBag = new HashBag<>();
        final Transformer<Object, Object> transformer = NOPTransformer.nopTransformer();
        final Bag<Object> transformedBag = TransformedBag.transformingBag(decoratedBag, transformer);

        final Object nonBagObject = Integer.valueOf(2);

        // Act
        final boolean isEqual = transformedBag.equals(nonBagObject);

        // Assert
        assertFalse("A TransformedBag should not be equal to an object that is not a Collection.", isEqual);
    }
}