package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    private static final double DELTA = 0.001;

    /**
     * Verifies that the getY() method correctly returns the y-value
     * that was provided to the constructor.
     */
    @Test
    public void getY_shouldReturnTheYValueProvidedInTheConstructor() {
        // Arrange: Create an XYInterval with a specific y-value.
        double xLow = 0.0;
        double xHigh = 0.0;
        double expectedY = 0.0;
        double yLow = 0.0;
        double yHigh = -4479.95885;
        XYInterval interval = new XYInterval(xLow, xHigh, expectedY, yLow, yHigh);

        // Act: Retrieve the y-value using the getter.
        double actualY = interval.getY();

        // Assert: The retrieved y-value should match the one set in the constructor.
        assertEquals(expectedY, actualY, DELTA);
    }
}