package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ModuloAxis} class, focusing on value-to-coordinate conversions.
 */
public class ModuloAxisTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the valueToJava2D method correctly maps data values to coordinates
     * when the axis has a "wrapped" display range (e.g., from 315° to 45°).
     */
    @Test
    public void valueToJava2D_shouldMapValuesToCoordinates_forWrappedRange() {
        // Arrange
        // Use a fixed range of 0-360, representing degrees in a circle.
        Range fixedRange = new Range(0, 360);
        ModuloAxis axis = new ModuloAxis("Angle (°)", fixedRange);

        // Set a display range that wraps around the 360/0 point, from 315° to 45°.
        // This represents a 90-degree view centered on 0°.
        axis.setDisplayRange(315, 45);

        // Define a standard plot area for the conversion.
        Rectangle2D plotArea = new Rectangle2D.Double(0, 0, 200, 100);
        RectangleEdge axisEdge = RectangleEdge.BOTTOM; // Horizontal axis

        // Act & Assert
        // The display range is 315-360 (45 units) and 0-45 (45 units).
        // The first half of the plot area (0 to 100) should map to 315°-360°.
        // The second half (100 to 200) should map to 0°-45°.

        // Test the start boundary of the display range
        assertEquals(0.0, axis.valueToJava2D(315.0, plotArea, axisEdge), DELTA);

        // Test the wrap-around point (360° is equivalent to 0°)
        assertEquals(100.0, axis.valueToJava2D(360.0, plotArea, axisEdge), DELTA);
        assertEquals(100.0, axis.valueToJava2D(0.0, plotArea, axisEdge), DELTA);

        // Test the end boundary of the display range
        assertEquals(200.0, axis.valueToJava2D(45.0, plotArea, axisEdge), DELTA);

        // Test a value that needs the modulo calculation to be mapped into the fixed range
        // 405° is equivalent to 45° (405 % 360 = 45)
        assertEquals(200.0, axis.valueToJava2D(405.0, plotArea, axisEdge), DELTA);

        // Test a value in the middle of the first section (315-360)
        assertEquals(50.0, axis.valueToJava2D(337.5, plotArea, axisEdge), DELTA);

        // Test a value in the middle of the second section (0-45)
        assertEquals(150.0, axis.valueToJava2D(22.5, plotArea, axisEdge), DELTA);
    }
}