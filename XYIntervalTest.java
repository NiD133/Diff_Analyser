package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Tests the {@code equals} method to ensure it correctly compares all fields.
     */
    @Test
    public void testEquals() {
        // Create two identical XYInterval objects
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        assertEquals(interval1, interval2, "Intervals with identical values should be equal");

        // Test inequality by modifying each field one by one
        interval1 = new XYInterval(1.1, 2.0, 3.0, 2.5, 3.5);
        assertNotEquals(interval1, interval2, "Intervals with different xLow should not be equal");
        interval2 = new XYInterval(1.1, 2.0, 3.0, 2.5, 3.5);
        assertEquals(interval1, interval2, "Intervals should be equal after updating xLow");

        interval1 = new XYInterval(1.1, 2.2, 3.0, 2.5, 3.5);
        assertNotEquals(interval1, interval2, "Intervals with different xHigh should not be equal");
        interval2 = new XYInterval(1.1, 2.2, 3.0, 2.5, 3.5);
        assertEquals(interval1, interval2, "Intervals should be equal after updating xHigh");

        interval1 = new XYInterval(1.1, 2.2, 3.3, 2.5, 3.5);
        assertNotEquals(interval1, interval2, "Intervals with different y should not be equal");
        interval2 = new XYInterval(1.1, 2.2, 3.3, 2.5, 3.5);
        assertEquals(interval1, interval2, "Intervals should be equal after updating y");

        interval1 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.5);
        assertNotEquals(interval1, interval2, "Intervals with different yLow should not be equal");
        interval2 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.5);
        assertEquals(interval1, interval2, "Intervals should be equal after updating yLow");

        interval1 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.6);
        assertNotEquals(interval1, interval2, "Intervals with different yHigh should not be equal");
        interval2 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.6);
        assertEquals(interval1, interval2, "Intervals should be equal after updating yHigh");
    }

    /**
     * Verifies that the {@link XYInterval} class is not cloneable.
     */
    @Test
    public void testCloning() {
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        assertFalse(interval instanceof Cloneable, "XYInterval should not be cloneable");
    }

    /**
     * Tests serialization and deserialization of an {@link XYInterval} instance.
     */
    @Test
    public void testSerialization() {
        XYInterval originalInterval = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        XYInterval deserializedInterval = TestUtils.serialised(originalInterval);
        assertEquals(originalInterval, deserializedInterval, "Deserialized interval should be equal to the original");
    }
}