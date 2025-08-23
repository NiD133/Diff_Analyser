package org.jfree.chart.block;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double DELTA = 0.01;

    /**
     * Verifies that adding a block to the arrangement does not alter the state
     * of the block itself. The add() method is intended to be a callback used
     * by a container, and this test confirms it has no side effects on the
     * provided block.
     */
    @Test
    public void addShouldNotModifyBlockState() {
        // Arrange
        GridArrangement arrangement = new GridArrangement(2, 2);
        LabelBlock block = new LabelBlock("Test Block");

        // Act
        arrangement.add(block, "Test Key");

        // Assert: Verify the block's properties remain unchanged from their defaults.
        assertNull("ID should be null by default", block.getID());
        assertEquals("Width should be 0.0 by default", 0.0, block.getWidth(), DELTA);
        assertEquals("Height should be 0.0 by default", 0.0, block.getHeight(), DELTA);
        assertNull("URL text should be null by default", block.getURLText());
        assertNull("Tool tip text should be null by default", block.getToolTipText());
        assertEquals("Content X offset should be 0.0 by default", 0.0, block.getContentXOffset(), DELTA);
        assertEquals("Content Y offset should be 0.0 by default", 0.0, block.getContentYOffset(), DELTA);
        assertEquals("Content alignment point should be CENTER by default",
                TextBlockAnchor.CENTER, block.getContentAlignmentPoint());
        assertEquals("Text anchor should be CENTER by default",
                RectangleAnchor.CENTER, block.getTextAnchor());
    }
}