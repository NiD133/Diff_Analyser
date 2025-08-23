package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Unit tests for {@link FixedOrderComparator}.
 * This class focuses on the behavior of the equals() method.
 */
public class FixedOrderComparatorTest {

    @Test
    public void equalsShouldReturnFalseWhenComparedToNull() {
        // Arrange: Create an empty comparator instance.
        // The generic type is irrelevant for this test, so we use a simple one like String.
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();

        // Act & Assert: Verify that the comparator is not equal to null,
        // which is a standard contract for the equals() method.
        assertFalse(comparator.equals(null));
    }
}