package org.jfree.chart.title;

import org.jfree.chart.block.Size2D;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ShortTextTitle} class.
 *
 * This refactored test retains the original's scaffolding (`ShortTextTitle_ESTest_scaffolding`)
 * as it may contain necessary setup from the test generation tool.
 */
public class ShortTextTitle_ESTestTest6 extends ShortTextTitle_ESTest_scaffolding {

    /**
     * Verifies that arrangeFN() returns a size of (0, 0) when the provided
     * fixed width is too small to display the full title text. A ShortTextTitle
     * is designed to render only if its entire text can fit without wrapping.
     */
    @Test
    public void arrangeWithInsufficientFixedWidthShouldReturnZeroSize() {
        // Arrange
        // A long title that will require more space than the constrained width.
        ShortTextTitle title = new ShortTextTitle("This is a very long title that will not fit");

        // A graphics context is required for font metrics calculation.
        // The image dimensions are arbitrary as we only need the context.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // A width constraint that is clearly too small for the title text.
        final double insufficientWidth = 1.0;

        // Act
        // The arrangeFN method calculates the required size for a fixed width.
        Size2D arrangedSize = title.arrangeFN(g2, insufficientWidth);

        // Assert
        // Since the text does not fit within the constrained width, the title
        // should report a size of zero to indicate it should not be drawn.
        assertEquals("Width should be 0.0 when text does not fit", 0.0, arrangedSize.getWidth(), 0.0);
        assertEquals("Height should be 0.0 when text does not fit", 0.0, arrangedSize.getHeight(), 0.0);
    }
}