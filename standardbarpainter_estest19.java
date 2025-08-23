package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that the paintBar method executes without throwing an exception
     * when provided with standard, valid inputs. This acts as a basic "smoke test"
     * to ensure the core painting logic can be invoked successfully.
     */
    @Test
    public void paintBarShouldExecuteWithoutErrorsForStandardInputs() {
        // Arrange: Set up the painter, a renderer, and a graphics context.
        StandardBarPainter barPainter = new StandardBarPainter();
        BarRenderer renderer = new GroupedStackedBarRenderer();

        // Use a standard Rectangle2D for the bar shape, which is clearer than
        // the original test's use of DefaultCaret.
        Rectangle2D barArea = new Rectangle2D.Double(0, 0, 10, 20);
        RectangleEdge barBaseEdge = RectangleEdge.BOTTOM;

        // Create a mock graphics environment to render into.
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        int series = 0;
        int item = 1;

        // Act: Call the method under test.
        barPainter.paintBar(g2, renderer, series, item, barArea, barBaseEdge);

        // Assert: No explicit assertion is needed.
        // The test's purpose is to confirm that the method call completes without
        // throwing an exception. The original test had a misleading assertion
        // on the renderer's default state, which was unrelated to the painter's
        // behavior, so it has been removed for clarity.
    }
}