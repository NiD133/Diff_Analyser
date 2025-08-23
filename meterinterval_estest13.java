package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link MeterInterval} class, focusing on equality checks.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the equals() method returns false for two MeterInterval instances
     * that have the same label and range but differ in their paint and stroke properties.
     */
    @Test
    public void equals_shouldReturnFalse_whenPaintAndStrokePropertiesDiffer() {
        // Arrange
        String commonLabel = "Normal";
        Range commonRange = new Range(0.0, 50.0);

        // Create an interval using the constructor that applies default visual properties.
        // The defaults are: outlinePaint=Color.YELLOW, outlineStroke=BasicStroke(2.0f), backgroundPaint=null.
        MeterInterval intervalWithDefaults = new MeterInterval(commonLabel, commonRange);

        // Create a second interval with the same label and range but with custom,
        // different visual properties.
        Paint customOutlinePaint = Color.GRAY;
        Stroke customOutlineStroke = null; // Different from the default BasicStroke
        Paint customBackgroundPaint = Color.LIGHT_GRAY; // Different from the default null
        MeterInterval intervalWithCustomProperties = new MeterInterval(
                commonLabel, commonRange, customOutlinePaint, customOutlineStroke, customBackgroundPaint);

        // Act & Assert
        // The two intervals should not be considered equal because their visual attributes differ.
        assertNotEquals(intervalWithDefaults, intervalWithCustomProperties);
    }
}