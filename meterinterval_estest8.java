package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * A test suite for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that getBackgroundPaint() returns the same paint object
     * that was provided in the constructor.
     */
    @Test
    public void getBackgroundPaintShouldReturnPaintSetInConstructor() {
        // Arrange: Define the parameters for creating a MeterInterval.
        // We use Color.darkGray, which was used in the original test.
        final Paint expectedBackgroundPaint = Color.darkGray;
        final Range range = new Range(0.0, 50.0);
        final String label = "Test Range";

        // Act: Instantiate MeterInterval with a specific background paint.
        final MeterInterval interval = new MeterInterval(label, range, Color.BLACK,
                new BasicStroke(1.0f), expectedBackgroundPaint);
        final Paint actualBackgroundPaint = interval.getBackgroundPaint();

        // Assert: The getter should return the exact paint object we passed in.
        assertSame("The returned background paint should be the same instance as the one provided to the constructor.",
                expectedBackgroundPaint, actualBackgroundPaint);
        
        // For completeness, also check for equality.
        assertEquals("The returned background paint should be equal to the one provided to the constructor.",
                expectedBackgroundPaint, actualBackgroundPaint);
    }
}