package org.jfree.chart.title;

import org.jfree.chart.block.Size2D;
import org.junit.Test;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Tests that the arrange() method returns a zero size when the title's
     * font is so large that it likely cannot be rendered. The ShortTextTitle
     * should gracefully handle this by indicating it requires no space.
     */
    @Test
    public void arrange_withExtremelyLargeFont_shouldReturnZeroSize() {
        // Arrange
        // Use an extremely large font size that is likely to exceed rendering capabilities.
        final int extremelyLargeFontSize = 2_121_918_366;
        final Font hugeFont = new Font("SansSerif", Font.PLAIN, extremelyLargeFontSize);

        ShortTextTitle title = new ShortTextTitle("Test Title");
        title.setFont(hugeFont);

        // To call arrange(), we need a Graphics2D context. A simple buffered image provides one.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Act
        // The arrange() method calculates the required size. For a ShortTextTitle,
        // if the text cannot fit or be rendered, it should return a size of (0, 0).
        Size2D calculatedSize = title.arrange(graphics);

        // Assert
        assertEquals("Width should be zero for an unrenderable font size", 0.0, calculatedSize.getWidth(), 0.0);
        assertEquals("Height should be zero for an unrenderable font size", 0.0, calculatedSize.getHeight(), 0.0);

        // Clean up resources
        graphics.dispose();
    }
}