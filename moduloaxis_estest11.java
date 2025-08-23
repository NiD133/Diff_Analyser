package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ModuloAxis_ESTestTest11 { // Retaining original class name for context

    /**
     * Verifies that valueToJava2D correctly maps a data value to the bottom edge
     * of the plotting area when the axis has been resized and inverted.
     */
    @Test
    public void valueToJava2D_whenAxisIsInvertedAndResized_mapsValueToBottomEdge() {
        // ARRANGE
        // 1. Setup a ModuloAxis with a fixed data range of [0.0, 1.0].
        // The axis initially has a visible range that matches the fixed range.
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // 2. Double the visible range. The range is expanded around its center (0.5),
        // resulting in a new visible range of [-0.5, 1.5].
        axis.resizeRange(2.0);
        assertFalse("Resizing the range should disable auto-ranging", axis.isAutoRange());

        // 3. Invert the axis. For a vertical axis, this means the minimum value
        // is at the top of the screen and the maximum value is at the bottom.
        axis.setInverted(true);

        // 4. Define the plotting area on the screen. For a vertical axis on the LEFT edge,
        // the relevant coordinates are Y=500 (top) to Y=500.05 (bottom).
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 500.0, 2.0, 0.05);
        RectangleEdge edge = RectangleEdge.LEFT;

        // 5. Define the input value and the expected coordinate.
        // The value -4.0 is outside the axis range. The ModuloAxis should map it
        // to a value within its range before converting to a screen coordinate.
        double valueToMap = -4.0;

        // For an inverted vertical axis, the maximum value of the axis range (1.5)
        // maps to the bottom of the data area (y + height). This test asserts
        // that the input value maps to this bottom edge coordinate.
        double expectedCoordinate = dataArea.getMaxY(); // 500.05

        // ACT
        double actualCoordinate = axis.valueToJava2D(valueToMap, dataArea, edge);

        // ASSERT
        assertEquals("The data value should map to the bottom edge of the data area",
                expectedCoordinate, actualCoordinate, 0.01);
    }
}