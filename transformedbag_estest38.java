package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the factory methods of {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that the transformedBag() factory method correctly wraps an empty bag,
     * resulting in a new empty bag.
     */
    @Test
    public void testFactoryMethodCreatesEmptyBagFromEmptySource() {
        // Arrange: Create an empty source bag and a transformer.
        final Bag<Integer> sourceBag = new HashBag<>();
        // The specific transformer doesn't matter here, as there are no elements to transform.
        final Transformer<Integer, Integer> transformer = ConstantTransformer.nullTransformer();

        // Act: Create a transformed bag from the empty source bag.
        final Bag<Integer> transformedBag = TransformedBag.transformedBag(sourceBag, transformer);

        // Assert: The resulting bag should be non-null and empty.
        assertNotNull("The factory method should not return a null bag.", transformedBag);
        assertTrue("A transformed bag created from an empty bag should also be empty.", transformedBag.isEmpty());
    }
}