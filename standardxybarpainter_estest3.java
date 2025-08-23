package org.jfree.chart.renderer.xy;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.api.RectangleEdge;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that calling paintBarShadow with a null Graphics2D object
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics2D_throwsNullPointerException() {
        // Arrange: Set up the painter and necessary parameters for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge barBaseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method under test with a null Graphics2D context.
        // The @Test annotation expects this call to throw a NullPointerException.
        painter.paintBarShadow(null, renderer, row, column, bar, barBaseEdge, pegShadow);

        // Assert: The exception is verified by the @Test(expected=...) annotation.
    }
}