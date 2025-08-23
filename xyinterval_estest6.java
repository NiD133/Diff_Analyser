package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all fields and that the
     * corresponding getter methods return the expected values.
     */
    @Test
    public void whenConstructed_thenGettersReturnCorrectValues() {
        // Arrange: Define distinct expected values for the interval.
        // Using unique values for each parameter helps ensure that each field is
        // assigned correctly and not mixed up with another.
        final double expectedXLow = 10.0;
        final double expectedXHigh = 20.0;
        final double expectedY = 15.0;
        final double expectedYLow = 12.0;
        final double expectedYHigh = 18.0;

        // Act: Create an instance of XYInterval with the test values.
        XYInterval interval = new XYInterval(
            expectedXLow,
            expectedXHigh,
            expectedY,
            expectedYLow,
            expectedYHigh
        );

        // Assert: Verify that each getter returns the value set in the constructor.
        assertEquals("X Low value should match constructor argument",
            expectedXLow, interval.getXLow(), DELTA);
        assertEquals("X High value should match constructor argument",
            expectedXHigh, interval.getXHigh(), DELTA);
        assertEquals("Y value should match constructor argument",
            expectedY, interval.getY(), DELTA);
        assertEquals("Y Low value should match constructor argument",
            expectedYLow, interval.getYLow(), DELTA);
        assertEquals("Y High value should match constructor argument",
            expectedYHigh, interval.getYHigh(), DELTA);
    }
}