package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that calling paintBarShadow with a null Graphics2D context
     * results in a NullPointerException. This is a crucial check to ensure
     * the method handles invalid arguments gracefully.
     */
    @Test
    public void paintBarShadow_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Set up the necessary objects for the method call.
        // We use simple, representative values as the specific details are not
        // relevant to this null-check test.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new BarRenderer();
        RectangularShape bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act & Assert: We expect a NullPointerException when the method is called.
        // The assertThrows construct clearly defines the expected exception and the
        // action that should trigger it.
        assertThrows(NullPointerException.class, () -> {
            painter.paintBarShadow(
                (Graphics2D) null, // The null argument under test
                renderer,
                row,
                column,
                bar,
                baseEdge,
                pegShadow
            );
        });
    }
}