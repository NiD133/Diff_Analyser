package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link FixedOrderComparator} class, focusing on its
 * handling of unknown objects.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that a known object is considered greater than an unknown object
     * when the unknown object behavior is set to {@code UnknownObjectBehavior.BEFORE}.
     */
    @Test
    public void compareKnownToUnknownWithBehaviorBeforeShouldReturnPositive() {
        // Arrange
        // Create a comparator where 'null' is the only known object.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>((Object) null);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

        final Object knownObject = null;
        final Object unknownObject = new Object(); // An object not in the comparator's fixed order.

        // Act
        // Compare the known object with the unknown one.
        final int result = comparator.compare(knownObject, unknownObject);

        // Assert
        // With BEFORE behavior, unknown objects are sorted first. Therefore, a known
        // object is "greater than" an unknown one.
        assertEquals("A known object should be greater than an unknown object", 1, result);
        
        // The comparator should become locked after the first comparison.
        assertTrue("Comparator should be locked after a comparison", comparator.isLocked());
    }
}