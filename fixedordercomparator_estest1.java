package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link FixedOrderComparator#equals(Object)} method.
 */
public class FixedOrderComparatorTest {

    @Test
    public void testEquals_returnsFalse_whenComparatorsHaveDifferentItems() {
        // Arrange
        // Create a comparator with a specific order of items.
        final FixedOrderComparator<String> comparatorWithItems = new FixedOrderComparator<>("Mercury", "Venus", "Earth");

        // Create an empty comparator, which has a different internal state.
        final FixedOrderComparator<String> emptyComparator = new FixedOrderComparator<>();

        // Act & Assert
        // An empty comparator should not be equal to a comparator configured with items.
        // The equals method should return false.
        assertFalse("An empty comparator should not be equal to one with items",
                    emptyComparator.equals(comparatorWithItems));
    }
}