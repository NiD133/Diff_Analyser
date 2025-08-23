package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on exception handling.
 */
public class StandardBarPainter_ESTestTest13 extends StandardBarPainter_ESTest_scaffolding {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException when the Graphics2D context is null.
     * This is expected behavior, as the method cannot perform any drawing operations without a graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowNPEForNullGraphics() {
        // Arrange: Create a painter instance and set up mock parameters for the method call.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new GanttRenderer();
        Rectangle2D bar = new Rectangle2D.Double(10, 20, 30, 40);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        boolean pegShadow = true;

        // Act & Assert: Call the method with a null Graphics2D context.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        painter.paintBarShadow(
                null,       // The null Graphics2D context that should trigger the exception
                renderer,
                0,          // row
                0,          // column
                bar,
                baseEdge,
                pegShadow
        );
    }
}