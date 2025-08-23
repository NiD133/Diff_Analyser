package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on edge cases and error handling.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that paintBarShadow throws a NullPointerException if the Graphics2D context is null.
     * This is expected behavior, as the method cannot perform any drawing operations without a valid graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowNullPointerExceptionWhenGraphicsIsNull() {
        // Arrange: Set up the painter and method arguments.
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new StackedXYBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        RectangleEdge base = RectangleEdge.TOP;
        boolean pegShadow = true;

        // Act: Call the method with a null Graphics2D context.
        // The @Test annotation asserts that a NullPointerException is thrown.
        painter.paintBarShadow(null, renderer, 0, 0, bar, base, pegShadow);
    }
}