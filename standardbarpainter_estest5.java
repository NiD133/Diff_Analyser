package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on exception handling.
 */
public class StandardBarPainter_ESTestTest5 extends StandardBarPainter_ESTest_scaffolding {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException if the Graphics2D context is null.
     * A null graphics context is an invalid argument, and the method should fail fast.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowNPEForNullGraphics() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new LayeredBarRenderer(); // A concrete renderer instance is required
        Rectangle2D bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        RectangleEdge base = RectangleEdge.BOTTOM;
        Graphics2D g2 = null; // The null argument that should cause the exception

        // Act: Call the method with a null Graphics2D object.
        // Assert: The @Test(expected) annotation handles the assertion that a NullPointerException is thrown.
        painter.paintBarShadow(g2, renderer, 0, 0, bar, base, false);
    }
}