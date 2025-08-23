package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Unit tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that paintBarShadow() throws a NullPointerException if the 'base'
     * edge is null. The base edge is required to calculate the shadow's position.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowShouldThrowExceptionForNullBaseEdge() {
        // Arrange: Set up the painter, renderer, and a mock graphics context.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new GroupedStackedBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(10, 20, 30, 40);

        // Create a dummy Graphics2D object to ensure the test focuses on the null 'base' argument.
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Act: Call the method with a null 'base' edge.
        // The method is expected to throw a NullPointerException.
        painter.paintBarShadow(g2, renderer, 0, 0, bar, null, true);

        // Assert: The @Test(expected) annotation handles the exception verification.
    }
}