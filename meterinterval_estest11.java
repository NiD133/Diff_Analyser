package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the equals() method returns false for two MeterInterval
     * objects that differ only by their background paint.
     */
    @Test
    public void equals_shouldReturnFalse_whenBackgroundPaintsDiffer() {
        // Arrange: Create two MeterInterval instances that are identical except for the background paint.
        String commonLabel = "Test Interval";
        Range commonRange = new Range(0.0, 100.0);
        Paint commonOutlinePaint = Color.BLACK;
        Stroke commonOutlineStroke = new BasicStroke(2.0f);

        MeterInterval interval1 = new MeterInterval(commonLabel, commonRange,
                commonOutlinePaint, commonOutlineStroke, Color.GREEN);

        // Create a second interval with a different background paint.
        MeterInterval interval2 = new MeterInterval(commonLabel, commonRange,
                commonOutlinePaint, commonOutlineStroke, Color.RED);

        // Act & Assert: The two intervals should not be considered equal.
        assertNotEquals("Intervals with different background paints should not be equal", interval1, interval2);
    }
}