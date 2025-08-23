package org.jfree.chart.renderer.xy;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.api.RectangleEdge;

/**
 * Tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException if the Graphics2D
     * context is null. Drawing operations are impossible without a valid graphics
     * context, so this is the expected behavior.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Float bar = new Rectangle2D.Float(1.0f, 2.0f, 3.0f, 4.0f);
        RectangleEdge baseEdge = RectangleEdge.TOP;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act
        // The method is called with a null Graphics2D object, which should trigger the exception.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);

        // Assert: A NullPointerException is expected, which is handled by the
        // @Test(expected=...) annotation. The test will fail if no exception is thrown.
    }
}