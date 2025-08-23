package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that valueToJava2D correctly calculates the coordinate for a value
     * on an inverted axis, especially when the drawing area has a negative width.
     */
    @Test
    public void valueToJava2D_withInvertedAxisAndNegativeWidthArea_shouldCalculateCorrectCoordinate() {
        // Arrange
        // 1. Define the axis with a fixed range and set it to be inverted.
        Range fixedRange = new Range(0.0, 1.0); // Equivalent to DateAxis.DEFAULT_DATE_RANGE
        ModuloAxis moduloAxis = new ModuloAxis("Test Modulo Axis", fixedRange);
        moduloAxis.setInverted(true);

        // 2. Resize the axis range, which also disables auto-ranging.
        double resizePercent = 2859.56;
        double anchorValue = 2187.2;
        moduloAxis.resizeRange(resizePercent, anchorValue);

        // 3. Define the drawing area and axis position.
        // Note: The drawing area has a negative width, which is a valid but unusual case.
        double plotAreaX = 4454.370088683;
        double plotAreaY = 4.0;
        double plotAreaWidth = -3835.51370066;
        double plotAreaHeight = 0.0;
        Rectangle2D plotArea = new Rectangle2D.Double(plotAreaX, plotAreaY, plotAreaWidth, plotAreaHeight);
        RectangleEdge axisEdge = RectangleEdge.TOP;

        // 4. Define the value to convert and the expected coordinate.
        double valueToConvert = 2.0;
        double expectedCoordinate = -2257.7788874723888;
        final double tolerance = 0.01;

        // Act
        double actualCoordinate = moduloAxis.valueToJava2D(valueToConvert, plotArea, axisEdge);

        // Assert
        // Check that the calculated coordinate matches the expected value.
        assertEquals("The Java2D coordinate should be calculated correctly for an inverted axis.",
                expectedCoordinate, actualCoordinate, tolerance);

        // Also, verify the side-effect that resizing the range disables auto-ranging.
        assertFalse("Resizing the range should disable auto-ranging.", moduloAxis.isAutoRange());
    }
}