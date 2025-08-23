package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the hashCode() method of the DateTimeComparator class.
 */
public class DateTimeComparatorHashCodeTest {

    /**
     * Tests that two DateTimeComparator instances that are considered equal
     * also produce the same hash code, fulfilling the hashCode/equals contract.
     */
    @Test
    public void hashCode_returnsSameValueForEqualComparators() {
        // Arrange: Create two separate but identical comparator instances.
        // Both are configured to compare up to the 'dayOfMonth' field.
        DateTimeFieldType limit = DateTimeFieldType.dayOfMonth();
        DateTimeComparator comparatorA = DateTimeComparator.getInstance(limit, limit);
        DateTimeComparator comparatorB = DateTimeComparator.getInstance(limit, limit);

        // Assert: First, confirm the two comparators are equal to each other.
        assertEquals("Comparators with identical limits should be equal.", comparatorA, comparatorB);

        // Assert: The core requirement of the hashCode contract.
        assertEquals("Equal comparators must produce the same hash code.", comparatorA.hashCode(), comparatorB.hashCode());
    }

    /**
     * Tests that two unequal DateTimeComparator instances are likely to have
     * different hash codes. While not a strict requirement of the hashCode contract,
     * a good implementation will produce different hash codes for unequal objects
     * to ensure good performance in hash-based collections.
     */
    @Test
    public void hashCode_returnsDifferentValuesForUnequalComparators() {
        // Arrange: Create two comparators with different limits.
        DateTimeComparator dayComparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.dayOfMonth());
        DateTimeComparator hourComparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());

        // Assert: First, confirm the two comparators are not equal.
        assertNotEquals("Comparators with different limits should not be equal.", dayComparator, hourComparator);

        // Assert: Check that their hash codes are also different.
        assertNotEquals("Unequal comparators should ideally produce different hash codes.", dayComparator.hashCode(), hourComparator.hashCode());
    }
}