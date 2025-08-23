package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link ModuloAxis} class, focusing on coordinate calculations.
 */
public class ModuloAxisTest {

    /**
     * Verifies that valueToJava2D calculates the correct coordinate for a value
     * after the axis's display range has been resized. This test case covers a
     * scenario where the axis is vertical (on the right edge) and has a wrapped
     * display range.
     */
    @Test
    public void valueToJava2D_afterResizingRange_shouldReturnCorrectCoordinate() {
        // Arrange
        // 1. Define the fixed range for the modulo axis. Data values will be mapped to this range.
        // The original test used DateAxis.DEFAULT_DATE_RANGE, which is equivalent to new Range(0.0, 1.0).
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // 2. Resize the axis display range. This is a key part of the setup and
        //    also disables the auto-ranging feature. The specific percentage is
        //    preserved from the original, machine-generated test case.
        axis.resizeRange(1.1565116986768456);

        // 3. Define the drawing area and the axis location.
        //    The axis is positioned on the right edge of a tall, thin rectangle.
        Rectangle2D dataArea = new Rectangle2D.Double(
                0.13377999998920131, 500.0, 1.0E-8, 0.13377999998920131);
        RectangleEdge edge = RectangleEdge.RIGHT;

        // 4. Define the data value to be converted to a 2D coordinate.
        double valueToConvert = 0.05;

        // Act
        // Convert the data value to its corresponding Java2D coordinate using the method under test.
        double actualCoordinate = axis.valueToJava2D(valueToConvert, dataArea, edge);

        // Assert
        // Verify that the calculated coordinate is correct and that auto-ranging is off as a side effect.
        double expectedCoordinate = 500.12804464945566;
        double delta = 0.01;
        
        assertEquals("The Java2D coordinate should be calculated correctly after resizing the range.",
                expectedCoordinate, actualCoordinate, delta);
        assertFalse("Auto-ranging should be disabled after a manual resize.",
                axis.isAutoRange());
    }
}