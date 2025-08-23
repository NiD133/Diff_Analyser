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
     * Verifies that the constructor correctly initializes all fields and that the
     * corresponding getter methods return the expected values.
     */
    @Test
    public void testConstructorAndGetters() {
        // Arrange: Define distinct values for the interval to ensure each field is tested correctly.
        double expectedXLow = 1.0;
        double expectedXHigh = 2.0;
        double expectedY = 3.0;
        double expectedYLow = 4.0;
        double expectedYHigh = 5.0;

        // Act: Create an instance of XYInterval using the defined values.
        XYInterval interval = new XYInterval(
            expectedXLow, 
            expectedXHigh, 
            expectedY, 
            expectedYLow, 
            expectedYHigh
        );

        // Assert: Verify that each getter returns the value set in the constructor.
        assertEquals("getXLow() should return the value provided in the constructor.", 
            expectedXLow, interval.getXLow(), DELTA);
        assertEquals("getXHigh() should return the value provided in the constructor.", 
            expectedXHigh, interval.getXHigh(), DELTA);
        assertEquals("getY() should return the value provided in the constructor.", 
            expectedY, interval.getY(), DELTA);
        assertEquals("getYLow() should return the value provided in the constructor.", 
            expectedYLow, interval.getYLow(), DELTA);
        assertEquals("getYHigh() should return the value provided in the constructor.", 
            expectedYHigh, interval.getYHigh(), DELTA);
    }
}