package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the hashCode() method of the {@link DateTimeComparator} class.
 */
public class DateTimeComparatorHashCodeTest {

    /**
     * Tests that the hashCode() method for the default comparator instance
     * adheres to the general contract of Object.hashCode().
     * Specifically, it checks for consistency (multiple calls return the same value)
     * and that equal objects have equal hash codes.
     */
    @Test
    public void hashCode_forDefaultInstance_isConsistentAndEqualsBased() {
        // Arrange: Get two references to the default singleton instance.
        // DateTimeComparator.getInstance() returns a singleton, so comparator1 and comparator2
        // will refer to the same object.
        DateTimeComparator comparator1 = DateTimeComparator.getInstance();
        DateTimeComparator comparator2 = DateTimeComparator.getInstance();

        // Assert:
        // 1. Consistency: Calling hashCode() multiple times on the same object
        //    should return the same value.
        assertEquals("hashCode must be consistent across multiple calls",
                comparator1.hashCode(), comparator1.hashCode());

        // 2. Equality: If two objects are equal according to the equals() method,
        //    they must have the same hash code.
        assertEquals("Two default instances should be equal", comparator1, comparator2);
        assertEquals("Equal objects must have the same hash code",
                comparator1.hashCode(), comparator2.hashCode());
    }
}