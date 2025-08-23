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
     * Verifies that paintBarShadow() throws a NullPointerException
     * when the Graphics2D argument is null, ensuring null-safety.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowExceptionWhenGraphics2DIsNull() {
        // Arrange: Set up the painter and necessary arguments for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = true;

        // Act: Call the method with a null Graphics2D context.
        // This is the action expected to cause an exception.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}