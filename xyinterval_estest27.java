package org.jfree.data.xy;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /** A small tolerance for floating-point comparisons. */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties and that
     * the corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define distinct values for the interval.
        double xLow = 1.0;
        double xHigh = 2.0;
        double y = 3.0;
        double yLow = 4.0;
        double yHigh = 5.0;

        // Act: Create a new XYInterval instance.
        XYInterval interval = new XYInterval(xLow, xHigh, y, yLow, yHigh);

        // Assert: Check that each getter returns the value provided to the constructor.
        assertEquals("The x-low value should match the constructor argument.", xLow, interval.getXLow(), DELTA);
        assertEquals("The x-high value should match the constructor argument.", xHigh, interval.getXHigh(), DELTA);
        assertEquals("The y-value should match the constructor argument.", y, interval.getY(), DELTA);
        assertEquals("The y-low value should match the constructor argument.", yLow, interval.getYLow(), DELTA);
        assertEquals("The y-high value should match the constructor argument.", yHigh, interval.getYHigh(), DELTA);
    }
}