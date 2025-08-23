package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.jfree.data.time.DateRange;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ModuloAxis_ESTestTest13 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Tests the valueToJava2D method after the axis's display range has been
     * significantly resized. The test sets up a "wrapped" axis where the display
     * range (e.g., for a compass, from 270 to 90 degrees) is conceptually different
     * from the underlying fixed data range [0.0, 1.0]. A large resize operation
     * creates a complex state, and the test verifies that a negative data value is
     * correctly converted to its 2D coordinate.
     */
    @Test(timeout = 4000)
    public void testValueToJava2DAfterLargeResizeOnWrappedRange() {
        // Arrange
        // 1. Define the fixed data range for the axis. DateAxis.DEFAULT_DATE_RANGE
        // provides a simple range of [0.0, 1.0].
        Range fixedRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // 2. Resize the axis's display range by a large, specific factor. This action
        // mutates the internal state of the axis, particularly its displayStart and
        // displayEnd values, and disables auto-ranging.
        double resizeFactor = 499.3112711;
        axis.resizeRange(resizeFactor);

        // 3. Define the plotting area and the value to be converted.
        Rectangle2D plotArea = new Rectangle2D.Double(-2588.490989243, -1638.3, 0.05, 1138.013594771);
        RectangleEdge axisEdge = RectangleEdge.BOTTOM;
        double valueToConvert = -2588.490989243;

        // Act
        // Convert the data value to its corresponding 2D coordinate within the plot area.
        double actualCoordinate = axis.valueToJava2D(valueToConvert, plotArea, axisEdge);

        // Assert
        // Verify that the calculated coordinate matches the expected result.
        double expectedCoordinate = -2588.552898269559;
        assertEquals(expectedCoordinate, actualCoordinate, 0.01);

        // Also, verify a side-effect of resizing: auto-ranging should be disabled.
        assertFalse("Resizing the range should disable auto-ranging.", axis.isAutoRange());
    }
}