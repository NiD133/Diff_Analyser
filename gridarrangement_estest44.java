package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the {@code arrangeRF} method throws an
     * {@code IndexOutOfBoundsException} when the block container holds fewer blocks
     * than the number of cells defined by the grid dimensions.
     * <p>
     * The arrangement logic expects to find a block for each cell in the
     * grid and will fail if it attempts to access a block that does not exist.
     * </p>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void arrangeRF_withFewerBlocksThanGridCells_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Create a 2x2 grid, which requires 4 blocks to be filled.
        GridArrangement gridArrangement = new GridArrangement(2, 2);

        BlockContainer containerWithInsufficientBlocks = new BlockContainer();
        // Add only one block, which is fewer than the 4 cells in the grid.
        containerWithInsufficientBlocks.add(new EmptyBlock(0, 0));

        // The graphics context and constraint are not relevant to triggering this exception,
        // as the error occurs during block retrieval before they are used.
        Graphics2D g2 = null;
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act: Attempt to arrange the container. This should fail because the
        // arrangement logic will try to access blocks at indices 1, 2, and 3,
        // but the container's block list only has an element at index 0.
        // The Assert step is handled by the @Test(expected=...) annotation.
        gridArrangement.arrangeRF(containerWithInsufficientBlocks, g2, constraint);
    }
}