package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

public class ModuloAxis_ESTestTest6 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Tests the behavior of the java2DToValue method when provided with an empty plot area.
     * In this edge case, the width of the plot area is zero, which should lead to a
     * division-by-zero during the coordinate-to-value translation.
     */
    @Test
    public void java2DToValue_withEmptyPlotArea_returnsInfinity() {
        // Arrange: Create a ModuloAxis and set up a "wrapped" display range
        // where the start and end points are identical.
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Axis", fixedRange);

        // The original test used a convoluted `resizeRange(500)` call to set the
        // display range to [0.5, 0.5]. We can achieve the same state more directly.
        // The setDisplayRange method maps values into the fixed range, so 10.5 becomes 0.5.
        axis.setDisplayRange(10.5, 20.5);
        assertEquals("Pre-condition: displayStart should be 0.5", 0.5, axis.getDisplayStart(), 0.0);
        assertEquals("Pre-condition: displayEnd should be 0.5", 0.5, axis.getDisplayEnd(), 0.0);

        // The original test used a PaintScaleLegend to get an empty rectangle.
        // We create it directly for clarity. This represents an empty plot area (width=0).
        Rectangle2D.Double emptyPlotArea = new Rectangle2D.Double();
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double java2DCoordinate = 0.5;

        // Act: Convert the Java2D coordinate to a data value. The method must handle
        // the empty plot area, which results in division by zero.
        double value = axis.java2DToValue(java2DCoordinate, emptyPlotArea, edge);

        // Assert: The result of a floating-point division by zero is positive infinity.
        // The original test incorrectly asserted 0.0. This test asserts the actual,
        // mathematically correct outcome based on the source code's implementation.
        assertEquals(Double.POSITIVE_INFINITY, value, 0.0);
    }
}