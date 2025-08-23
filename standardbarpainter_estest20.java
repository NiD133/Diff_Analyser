package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Contains tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that {@code paintBar} executes without throwing an exception when the
     * renderer has a null {@code GradientPaintTransformer}.
     * <p>
     * A robust painter should handle this configuration gracefully, as a renderer
     * might not always have a gradient transformer configured.
     */
    @Test
    public void paintBarShouldNotThrowExceptionWhenGradientPaintTransformerIsNull() {
        // Arrange: Set up the painter, a renderer, and a dummy graphics context.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new GroupedStackedBarRenderer();

        // This is the key condition for this test: ensure the painter can handle
        // a renderer with a null gradient paint transformer.
        renderer.setGradientPaintTransformer(null);

        // Create a mock graphics environment for the painting operation.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D bar = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        RectangleEdge barBaseEdge = RectangleEdge.TOP;
        int seriesIndex = 0;
        int itemIndex = 0;

        // Act: Call the method under test.
        painter.paintBar(graphics, renderer, seriesIndex, itemIndex, bar, barBaseEdge);

        // Assert: The test is successful if the 'paintBar' method completes without
        // throwing an exception. A NullPointerException would indicate a bug.
        // No explicit assert statement is needed for this verification.
    }
}