package org.jfree.data.xy;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties of the XYInterval.
     * The state of the object is confirmed by calling its getter methods.
     */
    @Test
    public void constructor_ShouldCorrectlyInitializeAllFields() {
        // Arrange: Define the expected values for the interval.
        final double expectedXLow = 10.0;
        final double expectedXHigh = 11.0;
        final double expectedY = 20.0;
        final double expectedYLow = 19.5;
        final double expectedYHigh = 20.5;

        // Act: Create a new XYInterval instance with the defined values.
        XYInterval interval = new XYInterval(
            expectedXLow, expectedXHigh, expectedY, expectedYLow, expectedYHigh
        );

        // Assert: Verify that each getter returns the value provided to the constructor.
        assertEquals(expectedXLow, interval.getXLow(), DELTA);
        assertEquals(expectedXHigh, interval.getXHigh(), DELTA);
        assertEquals(expectedY, interval.getY(), DELTA);
        assertEquals(expectedYLow, interval.getYLow(), DELTA);
        assertEquals(expectedYHigh, interval.getYHigh(), DELTA);
    }
}