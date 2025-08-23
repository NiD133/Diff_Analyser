package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that calling paintBarShadow with a null Graphics2D context
     * results in a NullPointerException. This is expected behavior as the
     * method cannot perform drawing operations without a graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics_shouldThrowNullPointerException() {
        // Arrange: Set up the necessary objects for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge barBaseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method under test with a null Graphics2D argument.
        // The @Test annotation will assert that a NullPointerException is thrown.
        painter.paintBarShadow(
                (Graphics2D) null,
                renderer,
                row,
                column,
                bar,
                barBaseEdge,
                pegShadow
        );
    }
}