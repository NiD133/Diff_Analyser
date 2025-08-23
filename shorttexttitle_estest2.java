package org.jfree.chart.title;

import org.jfree.chart.block.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ShortTextTitle} class.
 * This class focuses on verifying the layout and rendering behavior of short text titles.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that arrangeRR() returns a zero size when the title's text
     * is wider than the available width constraint. This is the core feature of
     * ShortTextTitle: it should not be rendered at all if it doesn't fit.
     */
    @Test
    public void arrangeRR_whenTextExceedsWidthConstraint_shouldReturnZeroSize() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("This is a title that is intentionally long");

        // Create a dummy Graphics2D context, which is necessary for measuring text dimensions.
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Define a very narrow width constraint that the title text cannot possibly fit into.
        // The height constraint is null, meaning it is unconstrained for this test.
        Range widthConstraint = new Range(0.0, 10.0);
        Range heightConstraint = null;

        // Act
        // Attempt to arrange the title within the given constraints.
        Size2D actualSize = title.arrangeRR(g2, widthConstraint, heightConstraint);

        // Assert
        // Because the text is too wide for the constraint, the resulting size should be zero.
        Size2D expectedSize = new Size2D(0.0, 0.0);
        assertEquals("The size should be zero when the text doesn't fit the width constraint",
                expectedSize, actualSize);
    }
}