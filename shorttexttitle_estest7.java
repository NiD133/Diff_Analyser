package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.block.Size2D;

/**
 * Unit tests for the {@link ShortTextTitle} class, focusing on layout arrangement.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that arrangeFN() returns a Size2D object that respects the provided
     * fixed width, even when the title's text is empty. The height should be zero.
     *
     * 'FN' in arrangeFN stands for arranging with a Fixed width and No height constraint.
     */
    @Test
    public void arrangeFN_withEmptyText_shouldReturnSizeWithGivenWidthAndZeroHeight() {
        // Arrange: Create a graphics context (required by the arrange method) and
        // a ShortTextTitle with an empty string.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        ShortTextTitle emptyTitle = new ShortTextTitle("");
        double fixedWidth = 0.4;

        // Act: Arrange the title's layout with the fixed width constraint.
        Size2D arrangedSize = emptyTitle.arrangeFN(graphics2D, fixedWidth);

        // Assert: The resulting size should have the specified width and zero height.
        final double delta = 0.001;
        assertEquals("The width of the arranged size should match the fixed width constraint.",
                fixedWidth, arrangedSize.width, delta);
        assertEquals("The height of an empty title should be zero.",
                0.0, arrangedSize.height, delta);
    }
}