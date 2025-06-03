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
     * Tests the equals method to ensure it correctly identifies equal and unequal DateRange objects.
     */
    @Test
    public void testEquals() {
        // Create two identical DateRange objects and verify they are equal
        DateRange range1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange range2 = new DateRange(new Date(1000L), new Date(2000L));
        assertEquals(range1, range2);
        assertEquals(range2, range1);

        // Modify the start date of range1 and verify they are not equal
        range1 = new DateRange(new Date(1111L), new Date(2000L));
        assertNotEquals(range1, range2);

        // Update range2 to match range1 and verify they are equal again
        range2 = new DateRange(new Date(1111L), new Date(2000L));
        assertEquals(range1, range2);

        // Modify the end date of range1 and verify they are not equal
        range1 = new DateRange(new Date(1111L), new Date(2222L));
        assertNotEquals(range1, range2);

        // Update range2 to match range1 and verify they are equal again
        range2 = new DateRange(new Date(1111L), new Date(2222L));
        assertEquals(range1, range2);
    }

    /**
     * Tests serialization and deserialization of a DateRange object to ensure equality is maintained.
     */
    @Test
    public void testSerialization() {
        // Serialize and deserialize a DateRange object, then verify equality
        DateRange original = new DateRange(new Date(1000L), new Date(2000L));
        DateRange deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized);
    }

    /**
     * Verifies that the DateRange class is not cloneable, as it is immutable.
     */
    @Test
    public void testClone() {
        // Check that DateRange does not implement Cloneable
        DateRange range = new DateRange(new Date(1000L), new Date(2000L));
        assertFalse(range instanceof Cloneable);
    }

    /**
     * Confirms that DateRange is immutable by ensuring changes to input dates do not affect the DateRange.
     */
    @Test
    public void testImmutable() {
        // Create a DateRange with specific start and end dates
        Date startDate = new Date(10L);
        Date endDate = new Date(20L);
        DateRange range = new DateRange(startDate, endDate);

        // Modify the original start date and verify the DateRange is unaffected
        startDate.setTime(11L);
        assertEquals(new Date(10L), range.getLowerDate());

        // Attempt to modify the end date through the DateRange and verify it is unaffected
        range.getUpperDate().setTime(22L);
        assertEquals(new Date(20L), range.getUpperDate());
    }
}