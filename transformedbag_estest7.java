package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link TransformedBag} class, focusing on the add(E, int) method.
 */
public class TransformedBag_ESTestTest7 { // Original class name is preserved

    /**
     * Tests that calling add(E, int) with a negative number of copies
     * does not modify the bag and returns false, as per the Bag interface contract.
     */
    @Test
    public void addWithNegativeCopiesShouldNotModifyBagAndReturnFalse() {
        // Arrange
        final SortedBag<Integer> baseBag = new TreeBag<>();
        // A transformer that attempts to convert any added element to the integer 100.
        final Transformer<Integer, Integer> transformer = ConstantTransformer.constantTransformer(100);
        final SortedBag<Integer> transformedBag = TransformedSortedBag.transformedSortedBag(baseBag, transformer);

        final Integer elementToAdd = 50;
        final int negativeCopies = -10;

        // Act
        final boolean wasAdded = transformedBag.add(elementToAdd, negativeCopies);

        // Assert
        assertFalse("add() with negative copies should return false.", wasAdded);
        assertTrue("Bag should remain empty after attempting to add negative copies.", transformedBag.isEmpty());
        assertEquals("The count of the element should remain 0.", 0, transformedBag.getCount(elementToAdd));
    }
}