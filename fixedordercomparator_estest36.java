package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test class contains the improved test case.
 * The original test was part of a larger, auto-generated suite.
 */
public class FixedOrderComparator_ESTestTest36 {

    /**
     * Tests that attempting to modify the comparator after a comparison has been made
     * (which locks it) throws an UnsupportedOperationException.
     */
    @Test
    public void addAsEqual_shouldThrowException_whenComparatorIsLockedAfterComparison() {
        // Arrange: Create a comparator and confirm its initial state is unlocked.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(new ArrayList<>());
        assertFalse("Comparator should not be locked before the first comparison.", comparator.isLocked());

        // To compare unknown objects without an exception, we must set the behavior.
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

        // Act: Perform a comparison. According to the class documentation,
        // this action should permanently lock the comparator from further modification.
        final Object obj1 = "object 1";
        final Object obj2 = "object 2";
        comparator.compare(obj1, obj2);

        // Assert: Verify that the comparator is now locked.
        assertTrue("Comparator should be locked after the first comparison.", comparator.isLocked());

        // Act & Assert: Attempt to modify the locked comparator and expect an exception.
        try {
            final Object existingObject = "any object";
            final Object newObject = "a new object";
            comparator.addAsEqual(existingObject, newObject);
            fail("Expected an UnsupportedOperationException because the comparator is locked.");
        } catch (final UnsupportedOperationException e) {
            // Verify the exception message is as expected.
            assertEquals("Cannot modify a FixedOrderComparator after a comparison", e.getMessage());
        }
    }
}