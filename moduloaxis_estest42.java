package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ModuloAxis} class.
 */
// The class name is kept to match the original file context.
public class ModuloAxis_ESTestTest42 {

    /**
     * Verifies that valueToJava2D returns the origin of the drawing area (0.0)
     * when the provided area has zero width and height. In this scenario, any
     * data value should map to the top-left corner of the area.
     */
    @Test
    public void valueToJava2DShouldReturnZeroForZeroSizedArea() {
        // Arrange
        // A standard range for a ModuloAxis, e.g., for degrees in a circle.
        Range range = new Range(0.0, 360.0);
        ModuloAxis moduloAxis = new ModuloAxis("Angle", range);

        // A drawing area with zero width and height is a key condition for this test.
        Rectangle2D.Double zeroSizedArea = new Rectangle2D.Double();
        
        // An arbitrary data value to be converted.
        double valueToConvert = 180.0;

        // Act
        // Convert the data value to a 2D coordinate using a null edge, which
        // can be a tricky case.
        double coordinate = moduloAxis.valueToJava2D(valueToConvert, zeroSizedArea, null);

        // Assert
        // For a zero-sized area, the resulting coordinate should always be the origin (0.0).
        assertEquals(0.0, coordinate, 0.01);
    }
}