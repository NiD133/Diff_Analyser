package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the constructor taking only a label and a range
     * sets the background paint to null by default.
     */
    @Test
    public void constructorWithLabelAndRange_ShouldSetDefaultBackgroundPaintToNull() {
        // Arrange: Define the properties for a new interval.
        // The specific values are not critical for this test.
        String label = "Normal";
        Range range = new Range(0.0, 50.0);

        // Act: Create a MeterInterval using the two-argument constructor.
        MeterInterval interval = new MeterInterval(label, range);

        // Assert: Check that the background paint has been initialized to its default null value.
        assertNull("The background paint should be null by default.", interval.getBackgroundPaint());
    }
}