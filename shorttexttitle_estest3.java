package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.block.Size2D;
import org.jfree.data.Range;

// The original class name and inheritance are preserved.
public class ShortTextTitle_ESTestTest3 extends ShortTextTitle_ESTest_scaffolding {

    /**
     * Tests that the arrangeRR method correctly calculates the title's size
     * when the text fits within the given width constraint. The 'RR' in arrangeRR
     * signifies that both width and height are constrained by a Range.
     */
    @Test
    public void arrangeRR_WhenTextFitsWithinWidthConstraint_ShouldReturnTextDimensions() {
        // Arrange
        final String titleText = "Not yet implemented.";
        // This expected width is specific to the text and the default font settings
        // (SansSerif, BOLD, 12) used by the title object in the test environment.
        final double expectedWidth = 134.0;
        final double delta = 0.01;

        ShortTextTitle title = new ShortTextTitle(titleText);

        // A dummy Graphics2D context is required for font metrics calculations.
        // The original test's method of creating a JFreeChart was overly complex.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Define a width constraint that is large enough to accommodate the text.
        // For ShortTextTitle, arrangeRR only checks if the text width is within
        // the upper bound of the widthRange.
        Range widthConstraint = new Range(200.0, 1000.0);
        Range heightConstraint = new Range(0.0, 100.0); // Height constraint is not critical here.

        // Act
        Size2D arrangedSize = title.arrangeRR(graphics, widthConstraint, heightConstraint);

        // Assert
        assertEquals("The calculated width should match the expected text width.",
                expectedWidth, arrangedSize.width, delta);
    }
}