package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the equals() method of {@link FixedOrderComparator}.
 */
public class FixedOrderComparator_ESTestTest17 {

    /**
     * Tests that the equals() method returns false when comparing two
     * FixedOrderComparator instances that were constructed with different
     * item orderings.
     */
    @Test
    public void equalsShouldReturnFalseForComparatorsWithDifferentOrderings() {
        // Arrange: Create two comparators with the same items but in a different order.
        // This setup is much clearer than the original's complex array manipulation
        // while testing the same core principle: inequality due to different internal order maps.
        final FixedOrderComparator<String> comparatorA = new FixedOrderComparator<>("Mercury", "Venus", "Earth");
        final FixedOrderComparator<String> comparatorB = new FixedOrderComparator<>("Earth", "Venus", "Mercury");

        // Act & Assert: The comparators should not be considered equal.
        assertFalse("Comparators with different orderings should not be equal", comparatorA.equals(comparatorB));

        // A more idiomatic and concise way to assert inequality:
        assertNotEquals(comparatorA, comparatorB);
    }
}