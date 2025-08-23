package org.apache.commons.collections4.comparators;

import org.apache.commons.collections4.comparators.FixedOrderComparator.UnknownObjectBehavior;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class focuses on the locking behavior of the FixedOrderComparator.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class FixedOrderComparatorLockingTest {

    /**
     * Tests that the comparator becomes locked after the first comparison,
     * preventing further modifications. The internal checkLocked() method should
     * throw an UnsupportedOperationException on a locked comparator.
     */
    @Test
    public void checkLockedShouldThrowExceptionAfterAComparisonIsMade() {
        // Arrange: Create a comparator configured to handle unknown objects without throwing an error.
        // The comparator is initialized with an empty order, so any object will be "unknown".
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(Collections.emptyList());
        comparator.setUnknownObjectBehavior(UnknownObjectBehavior.AFTER);

        // Act: Perform a comparison. This action should trigger the internal lock.
        comparator.compare(new Object(), new Object());

        // Assert: Verify that calling checkLocked() now throws an exception,
        // confirming the comparator is locked.
        try {
            comparator.checkLocked();
            fail("Expected an UnsupportedOperationException because the comparator should be locked.");
        } catch (final UnsupportedOperationException e) {
            // Check for the specific exception message to ensure the failure is for the correct reason.
            assertEquals("Cannot modify a FixedOrderComparator after a comparison", e.getMessage());
        }
    }
}