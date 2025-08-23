package org.jfree.chart.block;

import org.jfree.ui.Size2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class, focusing on its layout calculations.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Verifies that arranging a partially filled grid with a fixed width constraint
     * correctly calculates the total height.
     * <p>
     * This test case reveals a specific behavior: the total height appears to be
     * calculated as the number of rows multiplied by the maximum height of any single
     * block in the grid, even for rows that are empty.
     */
    @Test
    public void arrangeWithFixedWithForPartiallyFilledGrid() {
        // Arrange: Set up a 2x3 grid but add only one block to it.
        final int gridRows = 2;
        final int gridCols = 3;
        final double blockHeight = 5.0;
        final double blockWidth = 5.0;

        Block singleBlock = new EmptyBlock(blockWidth, blockHeight);

        GridArrangement arrangement = new GridArrangement(gridRows, gridCols);
        BlockContainer container = new BlockContainer(arrangement);
        container.add(singleBlock); // The grid is now 1/6 full.

        final double containerFixedWidth = 30.0;
        RectangleConstraint constraint = RectangleConstraint.NONE.toFixedWidth(containerFixedWidth);

        // Act: Arrange the container.
        Size2D arrangedSize = container.arrange(null, constraint);

        // Assert: Verify the final dimensions of the container.
        // The width should simply match the fixed width from the constraint.
        assertEquals(containerFixedWidth, arrangedSize.getWidth(), EPSILON, "Width should match the fixed constraint.");

        // The expected height calculation is the key part of this test.
        // The grid has 2 rows and the tallest block has a height of 5.0.
        // The resulting height of 10.0 implies that the height of *each* row
        // is based on the tallest block in the entire grid.
        // Calculation: 2 rows * 5.0 height = 10.0.
        double expectedHeight = gridRows * blockHeight;
        assertEquals(expectedHeight, arrangedSize.getHeight(), EPSILON,
                "Total height should be (number of rows * max block height).");
    }
}