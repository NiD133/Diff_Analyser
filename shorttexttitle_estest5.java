package org.jfree.chart.title;

import org.jfree.chart.block.Size2D;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that arranging a title with an empty text string still reserves
     * vertical space based on the default font's metrics. The `arrangeNN` method
     * arranges the title with no constraints on width or height.
     */
    @Test
    public void arrangeNN_withEmptyText_returnsSizeBasedOnFontMetrics() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("");
        
        // A graphics context is required to measure font dimensions.
        // We can create a dummy one from a temporary image.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Act
        Size2D resultingSize = title.arrangeNN(g2);

        // Assert
        // The expected height is derived from the default font (12pt bold SansSerif).
        // Even with empty text, the title should occupy the height of one line.
        final double expectedHeight = 15.0;
        assertEquals("The height of an empty title should match the font's line height.",
                expectedHeight, resultingSize.height, 0.01);
    }
}