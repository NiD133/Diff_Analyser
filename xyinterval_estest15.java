package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final double DELTA = 0.0000001;

    /**
     * Verifies that the constructor correctly initializes all interval properties
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void constructor_ShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the expected values for the interval.
        final double expectedXLow = -2082.436;
        final double expectedXHigh = -1.0;
        final double expectedY = 1907.7299979743054;
        final double expectedYLow = 0.0;
        final double expectedYHigh = 1.0;

        // Act: Create a new XYInterval instance with the defined values.
        XYInterval interval = new XYInterval(
            expectedXLow, 
            expectedXHigh, 
            expectedY, 
            expectedYLow, 
            expectedYHigh
        );

        // Assert: Check that each getter returns the value set in the constructor.
        assertEquals("The X low value should match the constructor argument.", 
            expectedXLow, interval.getXLow(), DELTA);
        assertEquals("The X high value should match the constructor argument.", 
            expectedXHigh, interval.getXHigh(), DELTA);
        assertEquals("The Y value should match the constructor argument.", 
            expectedY, interval.getY(), DELTA);
        assertEquals("The Y low value should match the constructor argument.", 
            expectedYLow, interval.getYLow(), DELTA);
        assertEquals("The Y high value should match the constructor argument.", 
            expectedYHigh, interval.getYHigh(), DELTA);
    }
}