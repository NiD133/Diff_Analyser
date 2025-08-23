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
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all fields and that the
     * corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldCorrectlyInitializeAllFields() {
        // Arrange: Define distinct values for the interval's properties.
        final double expectedXLow = 1.0;
        final double expectedXHigh = 2.0;
        final double expectedY = 3.0;
        final double expectedYLow = 4.0;
        final double expectedYHigh = 5.0;

        // Act: Create an instance of XYInterval with the defined values.
        XYInterval interval = new XYInterval(
            expectedXLow,
            expectedXHigh,
            expectedY,
            expectedYLow,
            expectedYHigh
        );

        // Assert: Verify that each getter returns the value provided to the constructor.
        assertEquals("The x-low value should match the constructor argument.",
            expectedXLow, interval.getXLow(), DELTA);
            
        assertEquals("The x-high value should match the constructor argument.",
            expectedXHigh, interval.getXHigh(), DELTA);
            
        assertEquals("The y-value should match the constructor argument.",
            expectedY, interval.getY(), DELTA);
            
        assertEquals("The y-low value should match the constructor argument.",
            expectedYLow, interval.getYLow(), DELTA);
            
        assertEquals("The y-high value should match the constructor argument.",
            expectedYHigh, interval.getYHigh(), DELTA);
    }
}