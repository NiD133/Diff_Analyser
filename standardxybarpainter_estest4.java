package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException when the
     * Graphics2D context is null, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowExceptionForNullGraphics() {
        // Arrange: Set up the painter and mock parameters for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        RectangleEdge base = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method with a null Graphics2D object.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        painter.paintBarShadow(null, renderer, row, column, bar, base, pegShadow);
    }
}