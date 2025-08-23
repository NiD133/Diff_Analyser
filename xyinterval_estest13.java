package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    private static final double TOLERANCE = 0.000001;

    /**
     * Verifies that the constructor correctly initializes all fields and that the
     * corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldSetAllFieldsCorrectly() {
        // Arrange: Define the expected values for the interval.
        final double expectedXLow = -2082.436;
        final double expectedXHigh = -1.0;
        final double expectedY = 1907.7299979743054;
        final double expectedYLow = 0.0;
        final double expectedYHigh = 1.0;

        // Act: Create a new XYInterval instance using the defined values.
        XYInterval interval = new XYInterval(
            expectedXLow,
            expectedXHigh,
            expectedY,
            expectedYLow,
            expectedYHigh
        );

        // Assert: Check that each getter returns the value set in the constructor.
        assertEquals("The x-low value should match the constructor argument.",
                expectedXLow, interval.getXLow(), TOLERANCE);
        assertEquals("The x-high value should match the constructor argument.",
                expectedXHigh, interval.getXHigh(), TOLERANCE);
        assertEquals("The y-value should match the constructor argument.",
                expectedY, interval.getY(), TOLERANCE);
        assertEquals("The y-low value should match the constructor argument.",
                expectedYLow, interval.getYLow(), TOLERANCE);
        assertEquals("The y-high value should match the constructor argument.",
                expectedYHigh, interval.getYHigh(), TOLERANCE);
    }
}