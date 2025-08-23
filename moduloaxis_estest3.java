package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that lengthToJava2D returns 0 when the drawing area has no height
     * for a vertical axis. The Java2D length should be proportional to the
     * available drawing space, so zero space must result in zero length.
     */
    @Test
    public void lengthToJava2DShouldReturnZeroForZeroHeightDrawingArea() {
        // Arrange: Create a ModuloAxis and a drawing area with zero height.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Angle Axis", fixedRange);

        // Define a drawing area with a width but zero height.
        Rectangle2D drawArea = new Rectangle2D.Double(0.0, 0.0, 200.0, 0.0);
        
        // The axis is positioned vertically on the left edge.
        RectangleEdge edge = RectangleEdge.LEFT;
        
        // An arbitrary length in the data's coordinate system.
        double dataLength = 90.0;

        // Act: Convert the data length to its Java2D representation.
        double java2dLength = axis.lengthToJava2D(dataLength, drawArea, edge);

        // Assert: The resulting length in Java2D coordinates must be 0.
        assertEquals(0.0, java2dLength, 0.0);
    }
}