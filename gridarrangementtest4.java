package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double DELTA = 1e-9;

    /**
     * This test verifies that when arranged with no constraints, the total size of the
     * container is calculated based on the dimensions of the largest block in the grid.
     * The total width is the largest block's width multiplied by the number of columns,
     * and the total height is the largest block's height multiplied by the number of rows.
     */
    @Test
    void arrange_withNoConstraint_shouldCalculateSizeBasedOnLargestBlock() {
        // Arrange
        // Create a 1x3 grid arrangement.
        int rowCount = 1;
        int columnCount = 3;
        BlockContainer container = new BlockContainer(new GridArrangement(rowCount, columnCount));

        // Add three blocks of different sizes. The largest block is 30x33.
        container.add(new EmptyBlock(10.0, 11.0));
        container.add(new EmptyBlock(20.0, 22.0));
        container.add(new EmptyBlock(30.0, 33.0)); // This is the largest block

        // The arrangement logic sizes each cell based on the largest block in the entire grid.
        double maxBlockWidth = 30.0;
        double maxBlockHeight = 33.0;
        double expectedWidth = maxBlockWidth * columnCount; // 30.0 * 3 = 90.0
        double expectedHeight = maxBlockHeight * rowCount;  // 33.0 * 1 = 33.0

        // Act
        // Arrange the container with no size constraints.
        Size2D arrangedSize = container.arrange(null, RectangleConstraint.NONE);

        // Assert
        assertEquals(expectedWidth, arrangedSize.width, DELTA,
                "Total width should be the max block width multiplied by the column count");
        assertEquals(expectedHeight, arrangedSize.height, DELTA,
                "Total height should be the max block height multiplied by the row count");
    }
}