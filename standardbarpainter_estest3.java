package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that calling paintBarShadow with a null Graphics2D context
     * results in a NullPointerException. This is a standard check for
     * argument validation.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange
        StandardBarPainter barPainter = new StandardBarPainter();
        BarRenderer renderer = new GroupedStackedBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act
        // The following call is expected to throw a NullPointerException because the
        // Graphics2D argument is null.
        barPainter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);

        // Assert: The exception is caught and verified by the @Test annotation.
    }
}