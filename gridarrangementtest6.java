package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * This test verifies the behavior of the GridArrangement when a fixed height
     * is applied with no width constraint. The expected behavior is that the
     * arrangement's final height matches the fixed constraint, and its width is
     * calculated based on the widest block in the grid. Specifically, all columns
     * are sized to the width of the widest block.
     */
    @Test
    @DisplayName("Arrange with fixed height should use widest block's width for all columns")
    void arrangeWithFixedHeightAndNoWidthConstraint_ShouldCalculateWidthBasedOnWidestBlock() {
        // Arrange
        // Create three blocks with different dimensions to be placed in a 1x3 grid.
        Block block1 = new EmptyBlock(10.0, 11.0);
        Block block2 = new EmptyBlock(20.0, 22.0);
        Block widestBlock = new EmptyBlock(30.0, 33.0); // The block with the maximum width.

        int numColumns = 3;
        var arrangement = new GridArrangement(1, numColumns);
        var container = new BlockContainer(arrangement);
        container.add(block1);
        container.add(block2);
        container.add(widestBlock);

        // Define a constraint with a fixed height and no constraint on the width.
        double fixedHeight = 100.0;
        RectangleConstraint constraint = RectangleConstraint.NONE.toFixedHeight(fixedHeight);

        // The GridArrangement makes all columns as wide as the widest block.
        // Expected width = (width of widest block) * (number of columns)
        //                = 30.0 * 3 = 90.0
        double expectedWidth = 30.0 * numColumns;
        double expectedHeight = fixedHeight;

        // Act
        // Arrange the container, which will calculate the final size.
        Size2D arrangedSize = container.arrange(null, constraint);

        // Assert
        assertEquals(expectedWidth, arrangedSize.width, EPSILON,
                "Width should be the number of columns multiplied by the widest block's width");
        assertEquals(expectedHeight, arrangedSize.height, EPSILON,
                "Height should be equal to the fixed height constraint");
    }
}