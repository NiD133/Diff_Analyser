package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the equals() method of TransformedBag and its subclasses.
 */
public class TransformedBagEqualsTest {

    /**
     * Tests that the equals() method returns true when comparing two different
     * but empty TransformedSortedBag instances.
     *
     * This test verifies that equality is correctly determined even when one
     * transformed bag is created by decorating another already transformed bag.
     */
    @Test
    public void testEqualsReturnsTrueForTwoEmptyAndEqualTransformedBags() {
        // Arrange
        // A transformer that converts any input to a constant value.
        // This transformer is required for instantiation but will not be invoked
        // as the bags remain empty throughout the test.
        final Transformer<Integer, Integer> transformer = ConstantTransformer.constantTransformer(165);

        // Create the first empty transformed bag.
        final SortedBag<Integer> firstTransformedBag =
                TransformedSortedBag.transformingSortedBag(new TreeBag<>(), transformer);

        // Create a second transformed bag by decorating the first one.
        final SortedBag<Integer> secondTransformedBag =
                TransformedSortedBag.transformedSortedBag(firstTransformedBag, transformer);

        // Act & Assert
        // The two bags should be considered equal because they are both empty.
        // The equals() method on the decorated collection should delegate to the
        // underlying collection's equals() method, which will find them equal.
        boolean areEqual = secondTransformedBag.equals(firstTransformedBag);
        assertTrue("Two empty transformed bags should be equal", areEqual);

        // A more idiomatic assertion using assertEquals provides a better failure message.
        assertEquals("The two bags should be equal", firstTransformedBag, secondTransformedBag);
    }
}