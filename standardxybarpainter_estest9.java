package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that the paintBar() method does not modify the dimensions of the
     * bar shape it is given, particularly when the bar has zero size. This
     * ensures the method has no unexpected side effects on its input parameters.
     */
    @Test
    public void paintBarWithZeroSizedBarShouldNotAlterBarDimensions() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new StackedXYBarRenderer();

        // Create a mock graphics context for the painting operation
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Create a bar with zero width and height
        Rectangle2D.Double zeroSizedBar = new Rectangle2D.Double(0, 0, 0, 0);
        RectangleEdge barBase = RectangleEdge.TOP;
        
        final int row = 0;
        final int column = 0;

        // Act
        // Attempt to paint the zero-sized bar. The primary goal is to ensure this
        // call completes without errors and doesn't change the bar's state.
        painter.paintBar(graphics, renderer, row, column, zeroSizedBar, barBase);

        // Assert
        // Confirm that the dimensions of the bar object remain unchanged.
        assertEquals("Bar width should not be modified after painting.", 0.0, zeroSizedBar.getWidth(), 0.0);
        assertEquals("Bar height should not be modified after painting.", 0.0, zeroSizedBar.getHeight(), 0.0);
    }
}