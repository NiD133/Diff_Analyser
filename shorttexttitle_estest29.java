package org.jfree.chart.title;

import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the ShortTextTitle class, focusing on layout and arrangement.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrange() method correctly calculates the title's size
     * when no width or height constraints are applied.
     */
    @Test
    public void arrange_withUnconstrainedBounds_returnsCorrectSize() {
        // Arrange
        // A Graphics2D object is needed for the title to calculate text dimensions.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        ShortTextTitle title = new ShortTextTitle("U");

        // Create a constraint that imposes no limits on width or height.
        // This is equivalent to the original test's setup, which used a null Range.
        RectangleConstraint unconstrained = new RectangleConstraint(null, null);

        // Act
        Size2D actualSize = title.arrange(g2d, unconstrained);

        // Assert
        // The expected width is based on the default font metrics for the text "U" plus padding.
        double expectedWidth = 11.0;
        assertEquals("The calculated width should match the expected value for unconstrained space.",
                expectedWidth, actualSize.width, 0.01);
    }
}