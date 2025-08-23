package org.jfree.chart.renderer.category;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.api.RectangleEdge;

/**
 * Contains tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainter_ESTestTest18 extends StandardBarPainter_ESTest_scaffolding {

    /**
     * Verifies that the paintBar() method executes without error when the renderer
     * is configured to draw a bar outline. This serves as a smoke test for this
     * specific code path.
     */
    @Test(timeout = 4000)
    public void paintBarWithOutlineEnabledShouldExecuteWithoutError() {
        // Arrange: Set up the painter, a renderer with outlines enabled, and a
        // mock graphics context.
        StandardBarPainter painter = new StandardBarPainter();
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        renderer.setDrawBarOutline(true); // This is the specific condition under test.

        // A mock graphics context is required for the painter to draw on.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        Rectangle2D.Double bar = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        RectangleEdge baseEdge = RectangleEdge.LEFT;
        int row = 0;
        int column = 1;

        // Act: Call the method under test. The test's primary goal is to ensure
        // this call completes without throwing an exception.
        painter.paintBar(g2, renderer, row, column, bar, baseEdge);

        // Assert: No explicit assertion is necessary. The test implicitly passes
        // if the 'Act' phase completes without throwing an exception. The original
        // test included a misleading assertion that was unrelated to the behavior
        // of the paintBar() method.
    }
}