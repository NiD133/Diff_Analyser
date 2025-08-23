package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that when a grid is not fully populated with blocks, its arranged
     * size is calculated correctly. The expected behavior is that the total size
     * is determined by the dimensions of the largest block, scaled by the number
     * of rows and columns in the grid. This test uses a grid with more cells
     * than blocks and no constraints.
     */
    @Test
    public void arrangeWithPartiallyFilledGridCalculatesSizeCorrectly() {
        // Arrange: Create a 2x3 grid and add a single 5x5 block.
        final int gridRows = 2;
        final int gridColumns = 3;
        final double blockWidth = 5.0;
        final double blockHeight = 5.0;

        Block singleBlock = new EmptyBlock(blockWidth, blockHeight);
        
        GridArrangement arrangement = new GridArrangement(gridRows, gridColumns);
        BlockContainer container = new BlockContainer(arrangement);
        container.add(singleBlock);

        // Act: Arrange the container with no constraints.
        Size2D arrangedSize = container.arrange(null, RectangleConstraint.NONE);

        // Assert: The total size should be based on the single block's dimensions
        // scaled by the grid size.
        double expectedWidth = blockWidth * gridColumns; // 5.0 * 3 = 15.0
        double expectedHeight = blockHeight * gridRows;  // 5.0 * 2 = 10.0

        assertEquals(expectedWidth, arrangedSize.getWidth(), DELTA,
                "Total width should be the max block width multiplied by the number of columns");
        assertEquals(expectedHeight, arrangedSize.getHeight(), DELTA,
                "Total height should be the max block height multiplied by the number of rows");
    }
}