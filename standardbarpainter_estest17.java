package org.jfree.chart.renderer.category;

import org.junit.Test;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.api.RectangleEdge;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on its handling of
 * invalid or edge-case arguments.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that calling paintBarShadow() with a null Graphics2D context
     * throws a NullPointerException. This is crucial because drawing operations
     * cannot proceed without a valid graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics_shouldThrowNullPointerException() {
        // Arrange: Set up the painter and mock parameters for the method call.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new GanttRenderer(); // A concrete BarRenderer implementation is needed.
        Rectangle2D.Double bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = true;

        // Act: Call the method under test with a null Graphics2D object.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);
    }
}