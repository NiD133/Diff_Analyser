package org.jfree.data.time;

import java.util.Date;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DateRange class.
 */
public class DateRangeTest {

    /**
     * Verifies that the equals method correctly identifies equal and unequal DateRange instances.
     */
    @Test
    public void testEqualsMethod() {
        // Create two DateRange instances with the same dates
        DateRange r1 = new DateRange(new Date(1000L), new Date(2000L));
        DateRange r2 = new DateRange(new Date(1000L), new Date(2000L));

        // Verify that the equals method returns true for equal instances
        assertEquals(r1, r2);
        assertEquals(r2, r1);

        // Create a DateRange instance with a different lower date
        r1 = new DateRange(new Date(1111L), new Date(2000L));

        // Verify that the equals method returns false for unequal instances
        assertNotEquals(r1, r2);

        // Create another DateRange instance with the same dates as r1
        r2 = new DateRange(new Date(1111L), new Date(2000L));

        // Verify that the equals method returns true for equal instances
        assertEquals(r1, r2);

        // Create a DateRange instance with a different upper date
        r1 = new DateRange(new Date(1111L), new Date(2222L));

        // Verify that the equals method returns false for unequal instances
        assertNotEquals(r1, r2);

        // Create another DateRange instance with the same dates as r1
        r2 = new DateRange(new Date(1111L), new Date(2222L));

        // Verify that the equals method returns true for equal instances
        assertEquals(r1, r2);
    }

    /**
     * Verifies that a DateRange instance can be serialized and deserialized correctly.
     */
    @Test
    public void testSerialization() {
        // Create a DateRange instance
        DateRange r1 = new DateRange(new Date(1000L), new Date(2000L));

        // Serialize and deserialize the DateRange instance
        DateRange r2 = TestUtils.serialised(r1);

        // Verify that the deserialized instance is equal to the original instance
        assertEquals(r1, r2);
    }

    /**
     * Verifies that the DateRange class is not cloneable.
     */
    @Test
    public void testCloneability() {
        // Create a DateRange instance
        DateRange r1 = new DateRange(new Date(1000L), new Date(2000L));

        // Verify that the DateRange instance is not cloneable
        assertFalse(r1 instanceof Cloneable);
    }

    /**
     * Verifies that the DateRange class is immutable.
     */
    @Test
    public void testImmutability() {
        // Create two Date instances
        Date d1 = new Date(10L);
        Date d2 = new Date(20L);

        // Create a DateRange instance with the two Date instances
        DateRange r = new DateRange(d1, d2);

        // Modify the first Date instance
        d1.setTime(11L);

        // Verify that the modification does not affect the DateRange instance
        assertEquals(new Date(10L), r.getLowerDate());

        // Modify the second Date instance through the DateRange instance
        r.getUpperDate().setTime(22L);

        // Verify that the modification does not affect the DateRange instance
        assertEquals(new Date(20L), r.getUpperDate());
    }
}