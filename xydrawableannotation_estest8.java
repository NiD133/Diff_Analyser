package org.jfree.chart.annotations;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.Drawable;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * A test case for the {@link XYDrawableAnnotation} class, focusing on its drawing behavior
 * under specific conditions.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the draw() method throws a NullPointerException when the range axis is null.
     * The range axis is a required parameter for calculating the drawable's y-position on the chart.
     */
    @Test(expected = NullPointerException.class)
    public void draw_withNullRangeAxis_shouldThrowNullPointerException() {
        // Arrange: Create an annotation and the necessary drawing context,
        // but intentionally set the rangeAxis to null.
        Drawable dummyDrawable = new BlockContainer();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 50.0, dummyDrawable);

        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 400, 300);
        ValueAxis domainAxis = new CyclicNumberAxis(10.0);
        ValueAxis rangeAxis = null; // This is the condition under test
        int rendererIndex = 0;
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        // A mock graphics context is required for the draw method to be called.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Act: Attempt to draw the annotation with the null range axis.
        // Assert: The test expects a NullPointerException, as declared in the @Test annotation.
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, rendererIndex, info);
    }
}