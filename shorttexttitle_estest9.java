package org.jfree.chart.title;

import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains an improved version of a test for the ShortTextTitle class.
 * Note: The original class name "ShortTextTitle_ESTestTest9" is preserved for context.
 * In a typical project, this test would be part of a comprehensive "ShortTextTitleTest" suite.
 */
public class ShortTextTitle_ESTestTest9 {

    /**
     * Verifies that the arrange() method correctly calculates the title's size
     * when a large negative top margin is applied, resulting in a negative
     * overall height.
     */
    @Test(timeout = 4000)
    public void arrangeShouldCalculateCorrectSizeWithNegativeTopMargin() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("U");

        // Define margins, including a large negative top margin to test the calculation.
        final double topMargin = -2187.894;
        final double bottomMargin = 17.0;
        final double leftMargin = 0.08;
        final double rightMargin = 0.08;
        title.setMargin(topMargin, leftMargin, bottomMargin, rightMargin);

        // A mock Graphics2D object is required for font metrics calculation during arrangement.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Use an unconstrained rectangle for the arrangement. This triggers the internal
        // logic for arranging with no width or height constraints.
        RectangleConstraint unconstrained = new RectangleConstraint(null, null);

        // Act
        // The arrange method calculates the required space for the title.
        Size2D arrangedSize = title.arrange(graphics, unconstrained);

        // Assert
        // The total height is the sum of the content height and the top and bottom margins.
        // In this specific test environment, the calculated height for the text "U"
        // with the default font is consistently 17.0.
        final double assumedContentHeight = 17.0;
        double expectedHeight = assumedContentHeight + topMargin + bottomMargin;

        assertEquals(
            "The height should be the sum of content height and vertical margins",
            expectedHeight,
            arrangedSize.height,
            0.01
        );
    }
}