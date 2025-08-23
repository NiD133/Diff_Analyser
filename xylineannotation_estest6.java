package org.jfree.chart.annotations;

import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link XYLineAnnotation} class, focusing on the draw method.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the draw() method throws a NullPointerException when the Graphics2D context is null.
     * The method should fail fast if it's given an invalid graphics context to draw on.
     */
    @Test(expected = NullPointerException.class)
    public void drawWithNullGraphics2DShouldThrowNullPointerException() {
        // Arrange: Create a plot, axes, and an annotation. The Graphics2D context is intentionally null.
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle();
        ValueAxis domainAxis = new CyclicNumberAxis(0, 10);
        ValueAxis rangeAxis = new DateAxis();
        XYLineAnnotation annotation = new XYLineAnnotation(
                1.0, 2.0, 3.0, 4.0, new BasicStroke(1.0f), Color.BLACK);
        Graphics2D g2 = null;
        PlotRenderingInfo info = null;

        // Act: Attempt to draw the annotation with a null Graphics2D object.
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, 0, info);

        // Assert: The test will pass if a NullPointerException is thrown, as declared in the @Test annotation.
    }
}