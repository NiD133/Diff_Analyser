package org.jfree.chart.annotations;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Tests that the draw() method executes without throwing an exception when
     * provided with a valid graphics context. This is a basic "smoke test" to ensure
     * the drawing logic doesn't crash under normal conditions.
     */
    @Test
    public void draw_withValidContext_shouldExecuteWithoutException() {
        // Arrange: Set up the annotation and a realistic drawing environment.
        XYPlot plot = new XYPlot();
        XYLineAnnotation annotation = new XYLineAnnotation(
                10.0, 1.0, 1.0, 10.0,
                plot.DEFAULT_OUTLINE_STROKE,
                plot.DEFAULT_CROSSHAIR_PAINT
        );

        // Create a mock graphics context by preparing to draw on a dummy image.
        BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        Rectangle dataArea = new Rectangle(0, 0, 200, 100);
        ValueAxis domainAxis = new PeriodAxis("Test Domain Axis");
        ValueAxis rangeAxis = new PeriodAxis("Test Range Axis");
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        // Act: Call the method under test.
        // The main goal is to ensure this call completes successfully.
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, 0, info);

        // Assert: No explicit assertions are needed.
        // The test succeeds if the 'draw' method does not throw an exception.
    }
}