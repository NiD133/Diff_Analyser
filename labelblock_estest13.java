package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Contains tests for the exception-throwing behavior of the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the draw() method throws a NullPointerException if the Graphics2D
     * context is null. Drawing operations are impossible without a valid graphics
     * context, so this is the expected behavior.
     */
    @Test(expected = NullPointerException.class)
    public void drawWithNullGraphics2DShouldThrowNullPointerException() {
        // Arrange: Create a LabelBlock instance and a drawing area.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        Rectangle2D drawArea = new Rectangle2D.Float();
        Graphics2D g2 = null;

        // Act: Attempt to draw the block with a null Graphics2D context.
        // Assert: A NullPointerException is expected, as declared by the @Test annotation.
        labelBlock.draw(g2, drawArea);
    }
}