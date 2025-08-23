package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method of {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that the equals() method returns false when comparing two
     * FixedOrderComparator instances that have different ordering rules.
     *
     * This test compares an empty comparator with one that has a defined order,
     * ensuring that their different internal states result in them being not equal.
     */
    @Test
    public void equalsShouldReturnFalseForComparatorsWithDifferentOrder() {
        // Arrange
        // Create an empty comparator. Its internal map of ordered items is empty.
        final FixedOrderComparator<String> emptyComparator = new FixedOrderComparator<>();

        // Create a second comparator with a defined order. Its internal map will not be empty.
        final FixedOrderComparator<String> comparatorWithOrder = new FixedOrderComparator<>("first", "second");

        // Act & Assert
        // The equals() method should return false because the internal state (the order map)
        // of the two comparators is different. We test both directions to ensure symmetry.
        assertFalse("An empty comparator should not be equal to a non-empty one.",
                    emptyComparator.equals(comparatorWithOrder));

        assertFalse("A non-empty comparator should not be equal to an empty one.",
                    comparatorWithOrder.equals(emptyComparator));
    }
}