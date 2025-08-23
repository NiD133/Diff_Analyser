package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on the paintBarShadow method.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that the paintBarShadow method executes without throwing an exception
     * when provided with a valid set of parameters. This test ensures the basic
     * rendering path is safe from runtime errors like NullPointerException.
     */
    @Test
    public void paintBarShadowShouldExecuteWithoutException() {
        // Arrange: Set up the painter, a renderer, and a graphics context.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        
        // Use a concrete XYBarRenderer implementation for the test.
        XYBarRenderer renderer = new StackedXYBarRenderer();
        
        // Create a dummy graphics target for the painter to draw on.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        
        // Define the shape of the bar and its base edge.
        Arc2D.Float bar = new Arc2D.Float(Arc2D.PIE);
        RectangleEdge barBase = RectangleEdge.RIGHT;
        final int row = 0;
        final int column = 0;
        final boolean pegShadow = false;

        // Act: Call the method under test.
        // The purpose is to confirm that this call completes successfully.
        painter.paintBarShadow(graphics, renderer, row, column, bar, barBase, pegShadow);

        // Assert: No explicit assertion is needed. The test succeeds if no exception is thrown.
    }
}