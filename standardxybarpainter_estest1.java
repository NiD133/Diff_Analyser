package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on shadow painting.
 */
public class StandardXYBarPainter_ESTestTest1 {

    /**
     * Verifies that paintBarShadow executes without throwing an exception when provided
     * with a bar whose base is on the left edge. This is a basic "smoke test" to ensure
     * the rendering logic handles this configuration correctly.
     */
    @Test
    public void paintBarShadowWithLeftEdgeBaseShouldExecuteWithoutError() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        // Use a non-default shadow offset to test the calculation logic.
        renderer.setShadowYOffset(279.48);

        // Create a minimal graphics context, as required by the method signature.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Define the bar shape and its orientation.
        Arc2D.Float bar = new Arc2D.Float(Arc2D.OPEN);
        RectangleEdge barBaseEdge = RectangleEdge.LEFT;
        
        // Arbitrary row/column values for the method call.
        int row = 1;
        int column = 1;
        boolean pegShadow = false;

        // Act & Assert
        // The test's purpose is to ensure that the method completes successfully.
        // A pass is indicated by the absence of any thrown exceptions. The original,
        // misleading assertion on a default renderer property has been removed.
        painter.paintBarShadow(graphics, renderer, row, column, bar, barBaseEdge, pegShadow);
    }
}