package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link FixedOrderComparator#equals(Object)}.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that the equals() method returns false when comparing a FixedOrderComparator
     * with an object of a completely different type.
     */
    @Test
    public void testEqualsShouldReturnFalseForDifferentObjectType() {
        // Arrange
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        final Object otherObject = new Object();

        // Act & Assert
        // The equals method should return false for any object that is not a FixedOrderComparator.
        assertFalse(comparator.equals(otherObject));
    }
}