package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * An improved version of the test for the ModuloAxis class, focusing on understandability.
 */
public class ModuloAxisTest {

    /**
     * Tests that java2DToValue() returns positive infinity when the plot area
     * has zero height for a vertical axis. This scenario is expected to cause a
     * division-by-zero in the underlying coordinate transformation logic.
     */
    @Test
    public void java2DToValueWithZeroHeightAreaShouldReturnInfinity() {
        // Arrange: Set up the axis and a degenerate plot area.
        final double resizePercent = 1005.89236;
        final double anchorValue = 0.13377999998920131;
        final double expectedUpperBoundAfterResize = 0.7675599999784026;

        // The ModuloAxis is initialized with a fixed range of 0.0 to 1.0.
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // This call resizes the axis's display range, putting it into the
        // specific state required by the original test case.
        axis.resizeRange(resizePercent, anchorValue);

        // A zero-height rectangle represents a degenerate plot area. For a vertical
        // axis (like one on the RIGHT edge), this will lead to a division by zero.
        Rectangle2D.Double zeroHeightPlotArea = new Rectangle2D.Double(0, 0, 100, 0);
        RectangleEdge edge = RectangleEdge.RIGHT;
        double arbitraryJava2DCoordinate = 0.8662200000107987;

        // Act: Attempt to convert a 2D coordinate to a data value using the
        // zero-height plot area.
        double convertedValue = axis.java2DToValue(arbitraryJava2DCoordinate, zeroHeightPlotArea, edge);

        // Assert: Verify the axis state and the result of the conversion.
        // 1. Confirm that the axis was resized as expected.
        assertEquals("The upper bound after resizing should match the expected value.",
                expectedUpperBoundAfterResize, axis.getUpperBound(), 1E-9);

        // 2. Confirm that the conversion correctly results in infinity.
        assertEquals("A coordinate conversion on a zero-height plot area should result in infinity.",
                Double.POSITIVE_INFINITY, convertedValue, 0.0);
    }
}