package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link StandardBarPainter} class, focusing on exception handling.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException when the Graphics2D context is null.
     * This is expected behavior, as the method cannot perform drawing operations without a valid graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowNullPointerExceptionWhenGraphicsContextIsNull() {
        // Arrange: Set up the test objects and parameters.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new StackedBarRenderer(); // A concrete renderer instance for the test.
        Rectangle2D bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge baseEdge = RectangleEdge.BOTTOM;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method under test with a null Graphics2D context.
        // This is the action that is expected to cause an exception.
        painter.paintBarShadow(null, renderer, row, column, bar, baseEdge, pegShadow);

        // Assert: The test passes if a NullPointerException is thrown, as specified
        // by the @Test(expected = ...) annotation. No further assertions are needed.
    }
}