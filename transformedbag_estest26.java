package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link TransformedBag} class, focusing on its
 * interaction with transformers and underlying bag implementations.
 */
public class TransformedBagTest {

    /**
     * Tests that adding an element to a TransformedBag throws a NullPointerException
     * if the transformer converts the element to null and the underlying bag
     * (in this case, a TreeBag) does not permit null elements.
     */
    @Test
    public void addShouldThrowNPEWhenTransformerReturnsNullForUnsupportedBag() {
        // Arrange
        // 1. Create a transformer that always returns null, simulating a transformation
        //    that results in a value unsupported by the decorated bag.
        final Transformer<Object, Integer> nullTransformer = ConstantTransformer.nullTransformer();

        // 2. Use a TreeBag as the underlying collection. TreeBag does not allow nulls.
        final TreeBag<Integer> underlyingBag = new TreeBag<>();

        // 3. Decorate the TreeBag with the transforming behavior.
        final TransformedSortedBag<Integer> transformedBag =
                new TransformedSortedBag<>(underlyingBag, nullTransformer);

        final Integer elementToAdd = 123; // An arbitrary element to be transformed.
        final int numberOfCopies = 5;

        // Act & Assert
        // The add operation should transform 'elementToAdd' to null. The underlying
        // TreeBag will then reject this null value, causing a NullPointerException.
        try {
            transformedBag.add(elementToAdd, numberOfCopies);
            fail("Expected a NullPointerException because the underlying TreeBag cannot store null elements.");
        } catch (final NullPointerException e) {
            // This exception is expected and confirms the correct behavior.
        }
    }
}