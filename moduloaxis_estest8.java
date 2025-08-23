package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

public class ModuloAxis_ESTestTest8 {

    /**
     * Tests the valueToJava2D() method for an inverted, "wrapped" axis.
     *
     * A "wrapped" axis is one where the display start value is greater than the
     * display end value (e.g., a compass showing 270-360-90 degrees). This test
     * verifies the coordinate calculation for a data value that falls into the
     * second segment of this wrapped range (the 0-90 part in the example).
     */
    @Test
    public void valueToJava2D_withInvertedWrappedAxis_shouldMapValueInSecondSegmentCorrectly() {
        // Arrange
        // 1. Define the axis's fixed numerical range.
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis moduloAxis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // 2. Configure the axis to be "wrapped" and inverted.
        // The default display range is 270.0 to 90.0, which is a wrapped range.
        // We set it explicitly here for clarity.
        moduloAxis.setDisplayRange(270.0, 90.0);
        moduloAxis.setInverted(true);

        // 3. Define the drawing area and the axis position (a vertical axis on the right).
        // Using simple coordinates makes the test easier to debug and understand.
        Rectangle2D plotArea = new Rectangle2D.Double(10, 50, 100, 200); // x, y, width, height
        RectangleEdge axisEdge = RectangleEdge.RIGHT;

        // 4. Choose a data value that falls within the second part of the wrapped
        // display range (i.e., value <= 90.0).
        double valueToConvert = 0.05;

        // Act
        double actualCoordinate = moduloAxis.valueToJava2D(valueToConvert, plotArea, axisEdge);

        // Assert
        // The expected value is calculated to match the specific logic within ModuloAxis
        // for a wrapped, inverted, vertical axis. The key is that the "total display length"
        // becomes negative, resulting in a coordinate slightly outside the plot area's minimum.
        double displayStart = 270.0;
        double displayEnd = 90.0;
        double fixedRangeLower = fixedRange.getLowerBound();
        double fixedRangeUpper = fixedRange.getUpperBound();

        double length1 = fixedRangeUpper - displayStart; // 1.0 - 270.0 = -269.0
        double length2 = displayEnd - fixedRangeLower;   // 90.0 - 0.0  = 90.0
        double totalDisplayLength = length1 + length2;   // -269.0 + 90.0 = -179.0

        double expectedCoordinate = plotArea.getMinY()
                + ((valueToConvert - fixedRangeLower) / totalDisplayLength) * plotArea.getHeight();
        // expected = 50.0 + ((0.05 - 0.0) / -179.0) * 200.0 = 49.944134...

        assertEquals(expectedCoordinate, actualCoordinate, 1E-8);
    }
}