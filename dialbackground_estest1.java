package org.jfree.chart.plot.dial;

import org.jfree.chart.plot.Plot;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the draw method correctly fills the specified view area
     * with the default background color (white).
     */
    @Test
    public void draw_withDefaultPaint_shouldFillViewRectangleWithWhite() {
        // Arrange
        DialBackground dialBackground = new DialBackground(); // Default paint is Color.WHITE

        // Create a test image and graphics context to draw on.
        int imageWidth = 100;
        int imageHeight = 100;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Pre-fill the image with a different color to ensure the draw method is responsible for the change.
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, imageWidth, imageHeight);

        // Define the area where the background should be drawn.
        Rectangle2D view = new Rectangle2D.Double(10, 20, 50, 60);

        // The 'plot' and 'frame' arguments are ignored by the draw method, so we can pass nulls or dummies.
        Plot plot = null;
        Rectangle2D frame = new Rectangle2D.Double();

        // Act
        dialBackground.draw(g2, (DialPlot) plot, frame, view);

        // Assert
        // Check a pixel inside the view rectangle to confirm it was painted white.
        int pixelInsideX = 25;  // 10 + 15
        int pixelInsideY = 45;  // 20 + 25
        assertEquals("Pixel inside the view area should be white.", Color.WHITE.getRGB(), image.getRGB(pixelInsideX, pixelInsideY));

        // Check a pixel outside the view rectangle to confirm it was not changed.
        int pixelOutsideX = 5;
        int pixelOutsideY = 5;
        assertEquals("Pixel outside the view area should remain unchanged.", Color.BLUE.getRGB(), image.getRGB(pixelOutsideX, pixelOutsideY));
    }
}