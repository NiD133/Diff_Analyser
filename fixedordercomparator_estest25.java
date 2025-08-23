package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FixedOrderComparator_ESTestTest25 {

    /**
     * Tests that comparing two different, unknown objects returns 0 (indicating they are equal)
     * when the unknown object behavior is set to BEFORE.
     * Also verifies that the comparator becomes locked after the first comparison.
     */
    @Test
    public void compareWithDifferentUnknownObjectsAndBeforeBehaviorShouldReturnZero() {
        // Arrange
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

        final Object unknownObject1 = new Object();
        final Object unknownObject2 = new Object();

        // Act
        final int result = comparator.compare(unknownObject1, unknownObject2);

        // Assert
        // With the BEFORE behavior, all unknown objects are considered equal to each other.
        assertEquals("Two different unknown objects should be treated as equal.", 0, result);

        // Any comparison should lock the comparator against further modification.
        assertTrue("The comparator should be locked after the first comparison.", comparator.isLocked());
    }
}