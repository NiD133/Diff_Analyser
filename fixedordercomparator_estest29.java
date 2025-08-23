package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains improved tests for the FixedOrderComparator.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class FixedOrderComparator_ESTestTest29 extends FixedOrderComparator_ESTest_scaffolding {

    /**
     * Tests that addAsEqual() returns true when adding a new object that is not
     * already in the comparator's known order.
     */
    @Test
    public void addAsEqualShouldReturnTrueWhenAddingNewObject() {
        // Arrange: Set up the test conditions.
        final Boolean existingItem = Boolean.FALSE;
        final Boolean newItem = Boolean.TRUE;

        // Create a comparator with a predefined order containing only the 'existingItem'.
        final FixedOrderComparator<Boolean> comparator = new FixedOrderComparator<>(existingItem);

        // Act: Call the method under test.
        // Attempt to add 'newItem' and make it compare as equal to 'existingItem'.
        final boolean wasAdded = comparator.addAsEqual(existingItem, newItem);

        // Assert: Verify the outcome.
        // The method should return true because 'newItem' was not previously known to the comparator.
        assertTrue("addAsEqual should return true for a new item.", wasAdded);
    }
}