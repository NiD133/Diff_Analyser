package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.util.Size2D;

/**
 * Contains unit tests for the {@link LabelBlock} class, focusing on layout and arrangement logic.
 */
public class LabelBlockTest {

    /**
     * Verifies that the arrange method correctly calculates the natural height of the block
     * when no constraints are applied. The expected height is determined by the metrics
     * of the default font used by the LabelBlock.
     */
    @Test
    public void testArrangeWithNoConstraintCalculatesCorrectNaturalHeight() {
        // Arrange: Create a LabelBlock, which uses a default font ("SansSerif", PLAIN, 10).
        LabelBlock labelBlock = new LabelBlock("Test Label");

        // The arrange method requires a Graphics2D context to measure the text.
        // A dummy 1x1 image is created solely to obtain this graphics context.
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dummyImage.createGraphics();

        // Use RectangleConstraint.NONE to allow the block to determine its own size.
        RectangleConstraint noConstraint = RectangleConstraint.NONE;

        // Act: Calculate the block's size.
        Size2D arrangedSize = labelBlock.arrange(g2, noConstraint);

        // Assert: The calculated height should match the expected height for the default font.
        // This value is based on the font's line metrics.
        final double expectedHeight = 13.0;
        assertEquals("The height should match the natural height of the text based on the default font.",
                expectedHeight, arrangedSize.getHeight(), 0.01);
    }
}