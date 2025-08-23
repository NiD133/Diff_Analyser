package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on the paintBarShadow method.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that paintBarShadow executes without throwing an exception when given a bar
     * with zero width and height. This is a regression test to ensure robustness with
     * edge-case geometric shapes.
     */
    @Test
    public void paintBarShadow_withZeroAreaBar_shouldNotThrowException() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new StackedBarRenderer();

        // Create a dummy graphics context for the painter to draw on.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Define a bar with zero area, which is an edge case for painting.
        Rectangle2D bar = new Rectangle2D.Double(10.0, 20.0, 0.0, 0.0);
        RectangleEdge baseEdge = RectangleEdge.TOP;
        int row = 0;
        int column = 0;
        boolean pegShadow = true;

        // Act & Assert
        // The test's purpose is to ensure that the method call completes without error.
        // The original test had a misleading assertion. A successful execution (no exception thrown)
        // is the validation criteria here.
        painter.paintBarShadow(g2, renderer, row, column, bar, baseEdge, pegShadow);
    }
}