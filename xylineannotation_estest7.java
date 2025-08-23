package org.jfree.chart.annotations;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link XYLineAnnotation} class, focusing on exception handling.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the draw() method throws a NullPointerException when the
     * Graphics2D context is null, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void draw_withNullGraphicsContext_shouldThrowNullPointerException() {
        // Arrange: Create a line annotation and the necessary plot objects for the draw method.
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);
        ValueAxis domainAxis = new NumberAxis("X");
        ValueAxis rangeAxis = new NumberAxis("Y");
        PlotRenderingInfo info = null; // PlotRenderingInfo is optional and can be null.
        int rendererIndex = 0;

        // Act: Call the draw method with a null Graphics2D context.
        // The @Test(expected=...) annotation will assert that a NullPointerException is thrown.
        annotation.draw(null, plot, dataArea, domainAxis, rangeAxis, rendererIndex, info);
    }
}