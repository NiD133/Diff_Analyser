package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that a newly created comparator is not locked by default.
     * A comparator only becomes locked after the first comparison is performed.
     */
    @Test
    public void newComparatorShouldNotBeLocked() {
        // Arrange: Create a new, empty comparator.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();

        // Act: Check the lock status.
        final boolean isLocked = comparator.isLocked();

        // Assert: The comparator should not be locked.
        assertFalse("A new comparator should not be locked upon instantiation", isLocked);
    }
}