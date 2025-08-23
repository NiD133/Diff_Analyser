package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ModuloAxis#lengthToJava2D(double, Rectangle2D, RectangleEdge)} method.
 */
public class ModuloAxisLengthToJava2DTest {

    private static final double DELTA = 1e-9;

    @Test
    public void lengthToJava2D_forVerticalAxis_shouldReturnProportionalPixelLength() {
        // Arrange
        // Define a simple data range for the axis, from 0.0 to 1.0.
        Range axisRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", axisRange);

        // Define a plotting area with a clear width and height.
        Rectangle2D plotArea = new Rectangle2D.Double(0, 0, 100, 200); // width=100, height=200

        // The axis is positioned vertically on the right edge.
        RectangleEdge edge = RectangleEdge.RIGHT;

        // We want to find the Java2D length for a data length that covers 10% of the axis range.
        double dataLength = 0.1;

        // Act
        // Convert the data length to its corresponding length in Java2D coordinates.
        double java2dLength = axis.lengthToJava2D(dataLength, plotArea, edge);

        // Assert
        // For a vertical axis, the Java2D length is proportional to the plot area's height.
        // The expected calculation is: (dataLength / totalRange) * plotHeight
        // Expected = (0.1 / 1.0) * 200.0 = 20.0
        double expectedLength = 20.0;
        assertEquals(expectedLength, java2dLength, DELTA);
    }

    @Test
    public void lengthToJava2D_forHorizontalAxis_shouldReturnProportionalPixelLength() {
        // Arrange
        // Define a data range for the axis, from 0.0 to 50.0.
        Range axisRange = new Range(0.0, 50.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", axisRange);

        // Define a plotting area with a clear width and height.
        Rectangle2D plotArea = new Rectangle2D.Double(0, 0, 300, 200); // width=300, height=200

        // The axis is positioned horizontally at the bottom.
        RectangleEdge edge = RectangleEdge.BOTTOM;

        // We want to find the Java2D length for a data length of 5.0 units.
        double dataLength = 5.0;

        // Act
        // Convert the data length to its corresponding length in Java2D coordinates.
        double java2dLength = axis.lengthToJava2D(dataLength, plotArea, edge);

        // Assert
        // For a horizontal axis, the Java2D length is proportional to the plot area's width.
        // The expected calculation is: (dataLength / totalRange) * plotWidth
        // Expected = (5.0 / 50.0) * 300.0 = 30.0
        double expectedLength = 30.0;
        assertEquals(expectedLength, java2dLength, DELTA);
    }
}