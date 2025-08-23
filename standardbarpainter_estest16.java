package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on exception handling.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that paintBarShadow throws a NullPointerException when the Graphics2D context is null.
     * This is expected behavior, as the method cannot perform any drawing operations without a valid graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new GanttRenderer(); // A concrete BarRenderer implementation for the test
        Rectangle2D bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act & Assert
        // The following call is expected to throw a NullPointerException because the first argument is null.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);
    }
}