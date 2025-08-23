package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

/**
 * Contains tests for the {@link ModuloAxis} class, focusing on specific
 * error conditions and edge cases.
 */
public class ModuloAxis_ESTestTest2 {

    /**
     * Verifies that lengthToJava2D() throws a NullPointerException if the plot
     * area argument is null. This is expected behavior, as the method requires
     * the area's dimensions to perform its calculations.
     */
    @Test(expected = NullPointerException.class)
    public void lengthToJava2DShouldThrowNPEForNullPlotArea() {
        // Arrange: Create a standard ModuloAxis instance.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Test Axis", fixedRange);
        Rectangle2D nullPlotArea = null;
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double lengthInDataCoords = 66.0;

        // Act & Assert: Call the method with a null area, which is expected
        // to throw a NullPointerException.
        axis.lengthToJava2D(lengthInDataCoords, nullPlotArea, edge);
    }
}