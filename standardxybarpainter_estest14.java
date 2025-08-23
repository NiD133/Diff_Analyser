package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that paintBarShadow executes without throwing an exception when the
     * base RectangleEdge is null.
     *
     * A robust implementation should handle null inputs gracefully. This test ensures
     * that a NullPointerException is not thrown in this specific scenario.
     */
    @Test
    public void paintBarShadowWithNullRectangleEdgeShouldNotThrowException() {
        // Arrange: Set up the painter and the necessary arguments for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();

        // Create a mock graphics environment to render on.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Create a renderer and a bar shape, which are required arguments.
        XYBarRenderer renderer = new StackedXYBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);

        // Define parameters for the method call. The base edge is intentionally null.
        int row = 0;
        int column = 0;
        RectangleEdge base = null;
        boolean pegShadow = false;

        // Act & Assert: Call the method and expect it to complete without throwing an exception.
        // The test will fail automatically if any unhandled exception is thrown.
        painter.paintBarShadow(graphics, renderer, row, column, bar, base, pegShadow);
    }
}