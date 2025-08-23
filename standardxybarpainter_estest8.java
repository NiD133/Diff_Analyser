package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Contains tests for the {@link StandardXYBarPainter} class.
 *
 * Note: The original test class name "StandardXYBarPainter_ESTestTest8" and scaffolding
 * are preserved as per the request's context.
 */
public class StandardXYBarPainter_ESTestTest8 extends StandardXYBarPainter_ESTest_scaffolding {

    /**
     * Verifies that paintBar() executes without throwing an exception when the
     * renderer is configured to draw a bar outline.
     *
     * The original auto-generated test included a meaningless assertion. This
     * refactored version clarifies the intent as a "smoke test" to ensure
     * the code path for drawing an outline is executable.
     */
    @Test(timeout = 4000)
    public void paintBarWithOutlineEnabledShouldExecuteSuccessfully() {
        // Arrange: Set up the painter, renderer, and graphics context.
        StandardXYBarPainter painter = new StandardXYBarPainter();

        // Use a standard renderer and enable the outline drawing feature.
        XYBarRenderer renderer = new StackedXYBarRenderer();
        renderer.setDrawBarOutline(true);

        // Create a mock graphics environment to act as a canvas.
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Define a simple bar shape and its orientation.
        Rectangle2D bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge barBaseEdge = RectangleEdge.BOTTOM;
        int seriesIndex = 0;
        int itemIndex = 0;

        // Act: Call the method under test.
        painter.paintBar(g2, renderer, seriesIndex, itemIndex, bar, barBaseEdge);

        // Assert: The test's purpose is to ensure the method call completes
        // without throwing an exception, which JUnit handles automatically.
        // No explicit assertion is needed.
    }
}