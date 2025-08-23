package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;

import org.apache.commons.collections4.comparators.FixedOrderComparator.UnknownObjectBehavior;
import org.junit.Test;

/**
 * Contains tests for the {@link FixedOrderComparator}.
 * This class focuses on the initial state and locking mechanism of the comparator.
 */
public class FixedOrderComparatorTest {

    /**
     * Verifies that calling checkLocked() on a newly created, unlocked comparator
     * does not throw an exception.
     */
    @Test
    public void checkLockedShouldNotThrowExceptionWhenComparatorIsNew() {
        // Arrange: Create a new comparator, which is unlocked by default.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();

        // Act & Assert: The call should complete without throwing an exception.
        // If it threw, the test would fail.
        comparator.checkLocked();
    }

    /**
     * Verifies that a new FixedOrderComparator defaults to the EXCEPTION behavior
     * for unknown objects.
     */
    @Test
    public void newComparatorShouldHaveDefaultUnknownObjectBehaviorOfException() {
        // Arrange: Create a new comparator.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();

        // Act: Retrieve the default behavior for handling unknown objects.
        final UnknownObjectBehavior actualBehavior = comparator.getUnknownObjectBehavior();

        // Assert: The default behavior should be to throw an exception.
        assertEquals(UnknownObjectBehavior.EXCEPTION, actualBehavior);
    }
}