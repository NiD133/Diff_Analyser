package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * This test suite contains tests for the {@link ModuloAxis} class.
 * This specific test case focuses on its behavior with a null range.
 */
public class ModuloAxis_ESTestTest7 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Verifies that valueToJava2D returns NaN when the ModuloAxis is initialized
     * with a null range. This is an important edge case, as a null range
     * could otherwise lead to a NullPointerException. The test also confirms
     * that the default display range is set correctly upon construction.
     */
    @Test
    public void valueToJava2DShouldReturnNaNWhenAxisRangeIsNull() {
        // Arrange: Create a ModuloAxis with a null fixedRange to simulate an
        // unconfigured state. Also, define a standard plotting area and edge.
        Range nullRange = null;
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", nullRange);
        Rectangle2D plotArea = new Rectangle2D.Double(0.0, 0.0, 100.0, 50.0);
        RectangleEdge edge = RectangleEdge.LEFT;
        
        // Act: Attempt to convert a data value to a 2D coordinate using the
        // axis with the null range.
        double coordinate = axis.valueToJava2D(19.0, plotArea, edge);

        // Assert: The method should handle the null range gracefully by returning NaN,
        // and the axis should retain its default display start and end values.
        assertEquals("Coordinate should be NaN for a null range", Double.NaN, coordinate, 0.01);
        assertEquals("Default display start value should be 270.0", 270.0, axis.getDisplayStart(), 0.01);
        assertEquals("Default display end value should be 90.0", 90.0, axis.getDisplayEnd(), 0.01);
    }
}