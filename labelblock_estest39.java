package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the draw() method does not alter the block's height.
     * The height of a block should only be calculated and set by the arrange() method.
     * This test ensures that draw() does not have the side effect of calculating
     * dimensions, and the height remains at its initial default value of 0.0.
     */
    @Test
    public void drawShouldNotAlterBlockHeight() {
        // Arrange: Create a LabelBlock. Its height is 0.0 by default until arrange() is called.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        Graphics2D mockGraphics = mock(Graphics2D.class);
        Rectangle2D drawArea = new Rectangle2D.Double();

        // Act: Call the draw method, which should only perform rendering operations.
        labelBlock.draw(mockGraphics, drawArea);

        // Assert: The height should remain unchanged from its initial value.
        assertEquals("The height of the block should not be changed by the draw() method.",
                0.0, labelBlock.getHeight(), 0.0);
    }
}