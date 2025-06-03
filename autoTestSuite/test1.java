package org.jfree.data.time;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DateRange} class.  These tests cover equality,
 * serialization, immutability and ensure the class behaves as expected.
 */
public class DateRangeTest {

    /**
     * Tests the {@link DateRange#equals(Object)} method to ensure that
     * DateRange objects with the same start and end dates are considered equal,
     * and those with different dates are not.
     */
    @Test
    public void testEquals() {
        // Create two DateRange objects with the same start and end dates.
        DateRange range1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange range2 = new DateRange(new Date(1000L), new Date(2000L));

        // Assert that the two ranges are equal.
        assertEquals(range1, range2);
        assertEquals(range2, range1);

        // Modify the start date of range1 and assert that the ranges are no longer equal.
        range1 = new DateRange(new Date(1111L), new Date(2000L));
        assertNotEquals(range1, range2);

        // Update range2 to match range1 and verify they are equal.
        range2 = new DateRange(new Date(1111L), new Date(2000L));
        assertEquals(range1, range2);

        // Modify the end date of range1 and assert that the ranges are no longer equal.
        range1 = new DateRange(new Date(1111L), new Date(2222L));
        assertNotEquals(range1, range2);

        // Update range2 to match range1 and verify they are equal.
        range2 = new DateRange(new Date(1111L), new Date(2222L));
        assertEquals(range1, range2);
    }

    /**
     * Tests the serialization and deserialization of a {@link DateRange} object.
     * Ensures that a serialized DateRange can be successfully restored with the same
     * start and end dates.
     */
    @Test
    public void testSerialization() {
        // Create a DateRange object.
        DateRange originalRange = new DateRange(new Date(1000L), new Date(2000L));

        // Serialize and deserialize the DateRange object.
        DateRange deserializedRange = TestUtils.serialised(originalRange);

        // Assert that the original and deserialized ranges are equal.
        assertEquals(originalRange, deserializedRange);
    }

    /**
     * Verifies that the {@link DateRange} class does not implement the {@link Cloneable}
     * interface, as DateRange objects are immutable.
     */
    @Test
    public void testClone() {
        // Create a DateRange object.
        DateRange range = new DateRange(new Date(1000L), new Date(2000L));

        // Assert that the DateRange object is not an instance of Cloneable.
        assertFalse(range instanceof Cloneable);
    }

    /**
     * Tests that the {@link DateRange} class is immutable. This means that modifying
     * the original Date objects used to create the DateRange should not affect the
     * DateRange object itself.
     */
    @Test
    public void testImmutable() {
        // Create Date objects.
        Date date1 = new Date(10L);
        Date date2 = new Date(20L);

        // Create a DateRange object using the Date objects.
        DateRange range = new DateRange(date1, date2);

        // Modify the original Date object after creating the DateRange.
        date1.setTime(11L);

        // Assert that the DateRange object still holds the original Date value.
        assertEquals(new Date(10L), range.getLowerDate());

        // Attempt to modify the upper date through the getter.
        range.getUpperDate().setTime(22L);

        // Assert that the DateRange object still holds the original Date value.
        assertEquals(new Date(20L), range.getUpperDate());
    }
}