package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException when the
     * Graphics2D context is null. A null graphics context is an invalid
     * argument, and the method should fail fast.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowNullPointerExceptionForNullGraphics() {
        // Arrange: Create the painter and the necessary parameters for the method call.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method with a null Graphics2D object.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);
    }
}