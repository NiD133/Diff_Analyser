package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on exception handling.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that paintBarShadow throws a NullPointerException when the Graphics2D context is null.
     * This is crucial for ensuring the method handles invalid input gracefully and fails fast.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics_shouldThrowNullPointerException() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D bar = new Rectangle(1, 2, 3, 4);
        RectangleEdge baseEdge = RectangleEdge.RIGHT;
        int row = 0;
        int column = 0;
        boolean pegShadow = true;

        // Act
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);

        // Assert: The test expects a NullPointerException, as specified by the @Test annotation.
    }
}