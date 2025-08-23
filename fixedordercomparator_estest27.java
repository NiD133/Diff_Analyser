package org.apache.commons.collections4.comparators;

import org.apache.commons.collections4.comparators.FixedOrderComparator.UnknownObjectBehavior;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// The original class name and inheritance are preserved to maintain compatibility
// with the existing test suite structure.
public class FixedOrderComparator_ESTestTest27 extends FixedOrderComparator_ESTest_scaffolding {

    /**
     * Tests that an unknown object is considered "less than" a known object
     * when the unknown object behavior is set to BEFORE.
     */
    @Test
    public void compare_whenUnknownObjectBehaviorIsBefore_shouldOrderUnknownObjectFirst() {
        // Arrange
        final String knownObject = "A"; // An object known to the comparator's order.
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(knownObject);
        comparator.setUnknownObjectBehavior(UnknownObjectBehavior.BEFORE);

        final String unknownObject = "B"; // An object not in the comparator's initial list.

        // Act
        // Compare the unknown object to the known object.
        final int result = comparator.compare(unknownObject, knownObject);

        // Assert
        // The result should be -1, indicating the unknown object comes before the known one.
        assertEquals("Unknown object should be ordered before the known object", -1, result);
        
        // A side-effect of compare() is that the comparator becomes locked.
        assertTrue("Comparator should be locked after the first comparison", comparator.isLocked());
    }
}