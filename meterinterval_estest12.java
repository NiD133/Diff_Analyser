package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import org.jfree.data.Range;

/**
 * Tests for the equals() method of the {@link MeterInterval} class.
 *
 * This class contains focused, understandable test cases that replace a
 * previously unclear, auto-generated test.
 */
public class MeterIntervalEqualsTest {

    /**
     * Verifies that two MeterInterval instances are not equal if their labels
     * are different, even when all other properties are identical.
     */
    @Test
    public void equals_shouldReturnFalse_whenLabelsAreDifferent() {
        // Arrange: Create two intervals with the same range but different labels.
        Range commonRange = new Range(0.0, 100.0);
        MeterInterval interval1 = new MeterInterval("Normal", commonRange);
        MeterInterval interval2 = new MeterInterval("Warning", commonRange);

        // Act & Assert: The intervals should not be considered equal.
        assertNotEquals("Intervals with different labels should not be equal", interval1, interval2);
    }

    /**
     * Verifies that two MeterInterval instances are not equal if their
     * background paints differ. This specifically tests the case where one
     * interval has the default null background paint and the other has a
     * non-null background paint.
     */
    @Test
    public void equals_shouldReturnFalse_whenBackgroundPaintsDiffer() {
        // Arrange: Define common properties for two intervals.
        String commonLabel = "Normal";
        Range commonRange = new Range(0.0, 100.0);

        // Arrange: Create an interval using the simple constructor, which results
        // in a default outline, a default stroke, and a null background paint.
        MeterInterval intervalWithDefaultBackground = new MeterInterval(commonLabel, commonRange);

        // Arrange: To isolate the change, retrieve the default paint and stroke
        // to create a second interval that is identical except for its background paint.
        Paint defaultOutlinePaint = intervalWithDefaultBackground.getOutlinePaint();
        Stroke defaultOutlineStroke = intervalWithDefaultBackground.getOutlineStroke();
        Paint customBackgroundPaint = Color.GREEN;

        MeterInterval intervalWithCustomBackground = new MeterInterval(
                commonLabel,
                commonRange,
                defaultOutlinePaint,
                defaultOutlineStroke,
                customBackgroundPaint
        );

        // Act & Assert: The intervals should not be equal due to the difference
        // in their background paint property.
        assertNotEquals("Intervals should not be equal if one has a null background paint and the other does not",
                intervalWithDefaultBackground, intervalWithCustomBackground);
    }
}