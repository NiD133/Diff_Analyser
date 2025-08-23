package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * A small tolerance for floating-point comparisons to handle potential precision issues.
     */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties of the
     * XYInterval object and that the corresponding getters return the correct values.
     */
    @Test
    public void constructor_ShouldCorrectlyInitializeAllFields() {
        // Arrange: Define the expected values for the interval.
        // Using distinct, simple values makes the test's purpose clear.
        final double expectedXLow = 10.0;
        final double expectedXHigh = 20.0;
        final double expectedY = 15.0;
        final double expectedYLow = 12.0;
        final double expectedYHigh = 18.0;

        // Act: Create a new XYInterval instance using the defined values.
        XYInterval interval = new XYInterval(
            expectedXLow,
            expectedXHigh,
            expectedY,
            expectedYLow,
            expectedYHigh
        );

        // Assert: Check that each getter returns the value set in the constructor.
        assertEquals("X Low should be correctly initialized.", expectedXLow, interval.getXLow(), DELTA);
        assertEquals("X High should be correctly initialized.", expectedXHigh, interval.getXHigh(), DELTA);
        assertEquals("Y value should be correctly initialized.", expectedY, interval.getY(), DELTA);
        assertEquals("Y Low should be correctly initialized.", expectedYLow, interval.getYLow(), DELTA);
        assertEquals("Y High should be correctly initialized.", expectedYHigh, interval.getYHigh(), DELTA);
    }
}