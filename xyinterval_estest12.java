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
     * Verifies that the getXLow() method returns the value
     * provided to the constructor.
     */
    @Test
    public void getXLow_ShouldReturnTheValueFromConstructor() {
        // Arrange: Define distinct values for the interval properties.
        final double expectedXLow = 10.0;
        final double xHigh = 20.0;
        final double y = 15.0;
        final double yLow = 12.0;
        final double yHigh = 18.0;

        XYInterval interval = new XYInterval(expectedXLow, xHigh, y, yLow, yHigh);

        // Act: Call the method under test.
        double actualXLow = interval.getXLow();

        // Assert: Check that the returned value is correct.
        assertEquals(expectedXLow, actualXLow, DELTA);
    }
}