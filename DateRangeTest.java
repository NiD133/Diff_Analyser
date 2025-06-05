package org.jfree.data.time;

import java.util.Date;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DateRange} class.
 */
public class DateRangeTest {

    /**
     * Tests that the equals method can distinguish between different date ranges.
     */
    @Test
    public void testEquals_DifferentRanges() {
        // Create two identical date ranges
        DateRange range1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange range2 = new DateRange(new Date(1000L), new Date(2000L));
        assertEquals(range1, range2);
        assertEquals(range2, range1);

        // Create a date range with a different lower bound
        DateRange range3 = new DateRange(new Date(1111L), new Date(2000L));
        assertNotEquals(range1, range3);

        // Create a date range with a different upper bound
        DateRange range4 = new DateRange(new Date(1000L), new Date(2222L));
        assertNotEquals(range1, range4);
    }

    /**
     * Tests that the equals method can distinguish between identical date ranges.
     */
    @Test
    public void testEquals_IdenticalRanges() {
        // Create two identical date ranges
        DateRange range1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange range2 = new DateRange(new Date(1000L), new Date(2000L));
        assertEquals(range1, range2);
    }

    /**
     * Tests that serialization and deserialization of a date range works correctly.
     */
    @Test
    public void testSerialization() {
        // Create a date range
        DateRange originalRange = new DateRange(new Date(1000L), new Date(2000L));

        // Serialize and deserialize the date range
        DateRange deserializedRange = TestUtils.serialised(originalRange);

        // Check that the original and deserialized date ranges are equal
        assertEquals(originalRange, deserializedRange);
    }

    /**
     * Tests that the DateRange class is not cloneable.
     */
    @Test
    public void testClone() {
        // Create a date range
        DateRange range = new DateRange(new Date(1000L), new Date(2000L));

        // Check that the date range is not cloneable
        assertFalse(range instanceof Cloneable);
    }

    /**
     * Tests that the DateRange class is immutable.
     */
    @Test
    public void testImmutable() {
        // Create a date range
        Date lowerDate = new Date(10L);
        Date upperDate = new Date(20L);
        DateRange range = new DateRange(lowerDate, upperDate);

        // Modify the lower date
        lowerDate.setTime(11L);

        // Check that the lower date in the date range has not changed
        assertEquals(new Date(10L), range.getLowerDate());

        // Modify the upper date
        Date upperDateInRange = range.getUpperDate();
        upperDateInRange.setTime(22L);

        // Check that the upper date in the date range has not changed
        assertEquals(new Date(20L), range.getUpperDate());
    }
}