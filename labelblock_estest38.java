package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that arranging a LabelBlock containing an empty string results
     * in a size of zero width and zero height.
     */
    @Test
    public void arrangeWithEmptyTextShouldReturnZeroSize() {
        // Arrange: Create a LabelBlock with no text and a mock Graphics2D context.
        LabelBlock emptyLabelBlock = new LabelBlock("");
        Graphics2D mockGraphics = mock(Graphics2D.class);
        
        // Use a simple, non-restrictive constraint. The result for an empty
        // label should be zero regardless of the constraint.
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act: Arrange the block to calculate its size.
        Size2D arrangedSize = emptyLabelBlock.arrange(mockGraphics, constraint);

        // Assert: A label with no text should have no dimensions.
        assertEquals("Width of an empty label block should be 0.0", 0.0, arrangedSize.getWidth(), 0.001);
        assertEquals("Height of an empty label block should be 0.0", 0.0, arrangedSize.getHeight(), 0.001);
    }
}