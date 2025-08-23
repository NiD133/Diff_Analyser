package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import org.jfree.chart.util.Size2D;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container with a 0x0 grid results in a size
     * with NaN (Not-a-Number) dimensions. This is an edge case where the grid has
     * no cells to arrange, leading to an undefined size.
     */
    @Test
    public void arrangeWithZeroByZeroGridAndEmptyContainerShouldReturnNaNSize() {
        // Arrange: Create a 0x0 grid, an empty container, and a "NONE" constraint,
        // which imposes no restrictions on size.
        GridArrangement gridArrangement = new GridArrangement(0, 0);
        BlockContainer emptyContainer = new BlockContainer();
        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        Graphics2D g2 = null; // The graphics context is not used in this scenario.

        // Act: Attempt to arrange the empty container using the 0x0 grid.
        // The protected method arrangeNF is called directly to isolate the test.
        Size2D arrangedSize = gridArrangement.arrangeNF(emptyContainer, g2, noConstraint);

        // Assert: The resulting size should have NaN for both width and height,
        // as arranging a grid with no cells is an undefined operation.
        assertEquals("Width should be NaN for a 0x0 grid arrangement",
                Double.NaN, arrangedSize.getWidth(), 0.0);
        assertEquals("Height should be NaN for a 0x0 grid arrangement",
                Double.NaN, arrangedSize.getHeight(), 0.0);
    }
}