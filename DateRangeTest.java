package org.jfree.data.time;

import java.util.Date;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DateRange} class.
 */
public class DateRangeTest {

    /**
     * Tests the {@code equals} method to ensure it correctly identifies
     * equal and unequal {@code DateRange} instances.
     */
    @Test
    public void testEquals() {
        // Test equality with identical date ranges
        DateRange range1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange range2 = new DateRange(new Date(1000L), new Date(2000L));
        assertEquals(range1, range2, "DateRanges with the same dates should be equal");
        assertEquals(range2, range1, "DateRanges with the same dates should be equal");

        // Test inequality with different lower bounds
        range1 = new DateRange(new Date(1111L), new Date(2000L));
        assertNotEquals(range1, range2, "DateRanges with different lower bounds should not be equal");

        // Test equality after updating range2 to match range1
        range2 = new DateRange(new Date(1111L), new Date(2000L));
        assertEquals(range1, range2, "DateRanges with the same dates should be equal");

        // Test inequality with different upper bounds
        range1 = new DateRange(new Date(1111L), new Date(2222L));
        assertNotEquals(range1, range2, "DateRanges with different upper bounds should not be equal");

        // Test equality after updating range2 to match range1
        range2 = new DateRange(new Date(1111L), new Date(2222L));
        assertEquals(range1, range2, "DateRanges with the same dates should be equal");
    }

    /**
     * Tests the serialization and deserialization of a {@code DateRange}
     * instance to ensure the object remains equal after the process.
     */
    @Test
    public void testSerialization() {
        DateRange originalRange = new DateRange(new Date(1000L), new Date(2000L));
        DateRange deserializedRange = TestUtils.serialised(originalRange);
        assertEquals(originalRange, deserializedRange, "Deserialized DateRange should be equal to the original");
    }

    /**
     * Verifies that the {@code DateRange} class is not cloneable, as it is
     * designed to be immutable.
     */
    @Test
    public void testClone() {
        DateRange range = new DateRange(new Date(1000L), new Date(2000L));
        assertFalse(range instanceof Cloneable, "DateRange should not be cloneable");
    }

    /**
     * Confirms that a {@code DateRange} instance is immutable by ensuring
     * that changes to the original dates do not affect the range.
     */
    @Test
    public void testImmutable() {
        Date lowerDate = new Date(10L);
        Date upperDate = new Date(20L);
        DateRange range = new DateRange(lowerDate, upperDate);

        // Attempt to modify the original lower date
        lowerDate.setTime(11L);
        assertEquals(new Date(10L), range.getLowerDate(), "Lower date should remain unchanged");

        // Attempt to modify the upper date through the range
        range.getUpperDate().setTime(22L);
        assertEquals(new Date(20L), range.getUpperDate(), "Upper date should remain unchanged");
    }
}