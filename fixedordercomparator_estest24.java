package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for {@link FixedOrderComparator}.
 * This class focuses on improving the understandability of a specific test case.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that a known object is considered "less than" an unknown object
     * when the unknown object behavior is set to AFTER.
     */
    @Test
    public void testCompareKnownToUnknownWithBehaviorAfterReturnsNegative() {
        // Arrange: Create a comparator that knows about a specific object (null in this case)
        // and is configured to place unknown objects AFTER known ones.
        final Object knownObject = null;
        final Object unknownObject = "some unknown object";

        // The comparator is initialized with only the 'knownObject'.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(knownObject);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

        // Act: Compare the known object to the unknown one.
        final int result = comparator.compare(knownObject, unknownObject);

        // Assert:
        // 1. The comparison result should be -1, as the known object comes before the unknown one.
        assertEquals("Known object should be less than the unknown object", -1, result);

        // 2. A side-effect of compare() is that the comparator becomes locked against further changes.
        assertTrue("Comparator should be locked after the first comparison", comparator.isLocked());
    }
}