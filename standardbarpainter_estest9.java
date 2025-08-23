package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Unit tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that the paintBar method throws a NullPointerException if the
     * Graphics2D context is null. A valid graphics context is essential for
     * any drawing operation.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShouldThrowNPEForNullGraphicsContext() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new StackedBarRenderer(); // A concrete renderer instance is needed
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int anyRow = 0;
        int anyColumn = 0;

        // Act & Assert
        // The method call should fail because the Graphics2D context is null.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        painter.paintBar(null, renderer, anyRow, anyColumn, null, baseEdge);
    }
}