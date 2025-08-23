package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Contains tests for the {@link StandardXYBarPainter} class, focusing on exception handling.
 * This is an improved version of an auto-generated test.
 */
public class StandardXYBarPainter_ESTestTest7 {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException when the
     * Graphics2D context is null. This is a critical check to ensure
     * robustness against invalid arguments.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Set up the necessary objects for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(10, 20, 30, 40);
        RectangleEdge baseEdge = RectangleEdge.TOP;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method with a null Graphics2D object.
        // The @Test annotation will assert that a NullPointerException is thrown.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);
    }
}