package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

/**
 * Contains tests for the {@link ModuloAxis} class, focusing on argument validation.
 */
public class ModuloAxisImprovedTest {

    /**
     * Verifies that the java2DToValue method throws a NullPointerException
     * when the plot area argument is null. This is crucial because the method
     * relies on the area's dimensions for its calculations.
     */
    @Test(expected = NullPointerException.class)
    public void java2DToValueShouldThrowExceptionForNullArea() {
        // Arrange: Create a standard ModuloAxis instance. The specific range and
        // label are not critical for this test, but meaningful values are used
        // for better readability.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Angle (°)", fixedRange);
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double arbitraryJava2DCoordinate = 100.0;
        Rectangle2D nullPlotArea = null;

        // Act & Assert: Call the method under test with a null area.
        // The test framework will automatically verify that a
        // NullPointerException is thrown.
        axis.java2DToValue(arbitraryJava2DCoordinate, nullPlotArea, edge);
    }
}