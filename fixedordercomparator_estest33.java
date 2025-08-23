package org.apache.commons.collections4.comparators;

import org.apache.commons.collections4.comparators.FixedOrderComparator.UnknownObjectBehavior;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link FixedOrderComparator}.
 * This specific test focuses on the behavior when comparing an unknown object
 * with a known one.
 */
public class FixedOrderComparatorTest {

    @Test
    public void compare_ShouldTreatUnknownObjectAsGreater_WhenBehaviorIsAfter() {
        // Arrange
        // Create a comparator where only 'null' is a known object.
        final Object[] knownOrder = { null };
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(knownOrder);

        // Set the comparator to place unknown objects AFTER known ones.
        comparator.setUnknownObjectBehavior(UnknownObjectBehavior.AFTER);

        final Object unknownObject = "unknown object";
        final Object knownObject = null;

        // Act
        // Compare an unknown object to a known one.
        final int result = comparator.compare(unknownObject, knownObject);

        // Assert
        // The unknown object should be considered "greater than" the known one, returning 1.
        assertEquals(1, result);

        // The comparator should also become locked after the first comparison.
        assertTrue("Comparator should be locked after the first compare() call", comparator.isLocked());
    }
}