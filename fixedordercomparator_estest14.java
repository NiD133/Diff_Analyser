package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest {

    @Test
    public void addShouldReturnTrueWhenAddingNewObject() {
        // Arrange
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();
        final Object newItem = new Object();

        // Act
        final boolean wasAdded = comparator.add(newItem);

        // Assert
        assertTrue("The add method should return true when a new object is successfully added.", wasAdded);
    }
}