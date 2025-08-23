package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DateTimeComparator} class, focusing on the
 * equals() and hashCode() contract.
 */
public class DateTimeComparatorTest {

    @Test
    public void testEqualsAndHashCodeContract() {
        // Arrange: Create different types of comparator instances to test against each other.
        DateTimeComparator defaultComparator = DateTimeComparator.getInstance();
        DateTimeComparator timeOnlyComparator1 = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator timeOnlyComparator2 = DateTimeComparator.getTimeOnlyInstance(); // A second instance of the same type
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();

        // --- Assert equals() contract ---

        // 1. Reflexivity: An object must be equal to itself.
        assertTrue("A comparator instance must be equal to itself.", defaultComparator.equals(defaultComparator));

        // 2. Symmetry: If x.equals(y) is true, then y.equals(x) must be true.
        // Also tests that two instances of the same type are equal.
        assertTrue("Two separate instances of a time-only comparator should be equal.", timeOnlyComparator1.equals(timeOnlyComparator2));
        assertTrue("Equality check must be symmetric.", timeOnlyComparator2.equals(timeOnlyComparator1));

        // 3. Inequality with different types
        assertFalse("A default comparator should not be equal to a time-only comparator.", defaultComparator.equals(timeOnlyComparator1));
        assertFalse("A default comparator should not be equal to a date-only comparator.", defaultComparator.equals(dateOnlyComparator));
        assertFalse("A time-only comparator should not be equal to a date-only comparator.", timeOnlyComparator1.equals(dateOnlyComparator));

        // 4. Inequality with null
        assertFalse("A comparator instance must not be equal to null.", defaultComparator.equals(null));

        // --- Assert hashCode() contract ---

        // 1. Consistency: If two objects are equal, they must have the same hash code.
        assertEquals("Equal time-only comparator instances must have the same hash code.",
                timeOnlyComparator1.hashCode(), timeOnlyComparator2.hashCode());

        // 2. Inequality: If two objects are not equal, their hash codes are not required
        // to be different, but it's good practice for them to be.
        assertNotEquals("Unequal default and time-only comparators should have different hash codes.",
                defaultComparator.hashCode(), timeOnlyComparator1.hashCode());
        assertNotEquals("Unequal time-only and date-only comparators should have different hash codes.",
                timeOnlyComparator1.hashCode(), dateOnlyComparator.hashCode());
    }
}