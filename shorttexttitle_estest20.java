package org.jfree.chart.title;

import org.jfree.chart.block.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Tests that the arrangeRN() method returns a size of (0, 0) when the provided
     * width range is too small to accommodate the title's text. A ShortTextTitle
     * is designed to not display at all if it cannot fit completely.
     */
    @Test
    public void arrangeRNShouldReturnZeroSizeWhenWidthRangeIsTooSmall() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("This is a test title");

        // A Graphics2D object is needed for font metrics calculations.
        // We can create a minimal one from a temporary image.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        // Define a width range that is clearly too narrow for the text.
        Range insufficientWidthRange = new Range(0.0, 10.0);

        // Act
        Size2D calculatedSize = title.arrangeRN(graphics2D, insufficientWidthRange);

        // Assert
        // The title should report a size of zero if it cannot fit.
        assertEquals("Width should be 0.0 when text does not fit", 0.0, calculatedSize.getWidth(), 0.0);
        assertEquals("Height should be 0.0 when text does not fit", 0.0, calculatedSize.getHeight(), 0.0);
        
        // Clean up graphics resources
        graphics2D.dispose();
    }
}