package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the GridArrangement class.
 * This test focuses on the arrangeFR method.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container within a zero-sized constraint
     * results in a size of zero.
     */
    @Test
    public void arrangeFR_withEmptyContainerAndZeroSizedConstraint_returnsZeroSize() {
        // Arrange: Set up an arrangement for an empty container with a constraint
        // that effectively allows zero width and zero height.
        GridArrangement arrangement = new GridArrangement(15, 15);
        BlockContainer emptyContainer = new BlockContainer();
        
        // A constraint with a zero-length range for both width and height defines a 0x0 area.
        Range zeroRange = new Range(0.0, 0.0);
        RectangleConstraint zeroSizeConstraint = new RectangleConstraint(zeroRange, zeroRange);

        // Act: Arrange the empty container. The arrangeFR method is for a Fixed-width,
        // Ranged-height scenario, but the core logic should handle this edge case gracefully.
        Size2D arrangedSize = arrangement.arrangeFR(emptyContainer, null, zeroSizeConstraint);

        // Assert: The resulting arranged size should be 0.0 x 0.0.
        assertNotNull("The arranged size should not be null.", arrangedSize);
        assertEquals("Width should be zero for a zero-sized constraint.", 0.0, arrangedSize.getWidth(), 0.0);
        assertEquals("Height should be zero for a zero-sized constraint.", 0.0, arrangedSize.getHeight(), 0.0);
    }
}