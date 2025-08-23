package org.jfree.data.xy;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    private static final double TOLERANCE = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties and that
     * the corresponding getter methods return the expected values.
     */
    @Test
    public void testConstructorAndGetters() {
        // Arrange: Define distinct values for the interval properties.
        final double xLow = 1.1;
        final double xHigh = 2.2;
        final double y = 3.3;
        final double yLow = 4.4;
        final double yHigh = 5.5;

        // Act: Create a new XYInterval instance.
        XYInterval interval = new XYInterval(xLow, xHigh, y, yLow, yHigh);

        // Assert: Check that each getter returns the value provided to the constructor.
        assertEquals("The x-low value should match the constructor argument.",
                xLow, interval.getXLow(), TOLERANCE);
        assertEquals("The x-high value should match the constructor argument.",
                xHigh, interval.getXHigh(), TOLERANCE);
        assertEquals("The y-value should match the constructor argument.",
                y, interval.getY(), TOLERANCE);
        assertEquals("The y-low value should match the constructor argument.",
                yLow, interval.getYLow(), TOLERANCE);
        assertEquals("The y-high value should match the constructor argument.",
                yHigh, interval.getYHigh(), TOLERANCE);
    }
}