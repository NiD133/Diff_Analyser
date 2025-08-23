package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException if the
     * Graphics2D context is null. This is a critical guard condition to prevent
     * unexpected runtime errors during chart rendering.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new BarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method with a null Graphics2D object.
        // The @Test(expected=...) annotation handles the assertion.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);
    }
}