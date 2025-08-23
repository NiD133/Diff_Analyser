package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

/**
 * Contains tests for the {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that the add() method returns false when attempting to add an item
     * that is already present in the comparator's fixed order.
     */
    @Test
    public void addShouldReturnFalseForExistingItem() {
        // Arrange: Create a comparator with a predefined order of items.
        List<String> initialOrder = Arrays.asList("Mercury", "Venus", "Earth");
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(initialOrder);
        
        String existingItem = "Venus";

        // Act: Attempt to add an item that is already part of the order.
        boolean wasAdded = comparator.add(existingItem);

        // Assert: The add method should return false, as the item was already known
        // to the comparator and its position was just updated.
        assertFalse("Adding an existing item should return false.", wasAdded);
    }
}