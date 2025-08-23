package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the sizing behavior of the {@link GridArrangement} class.
 */
@DisplayName("GridArrangement Sizing")
class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    private BlockContainer container;

    /**
     * Sets up a container with a 1x3 grid arrangement and three blocks of different sizes.
     * This setup is used by the test method.
     */
    @BeforeEach
    void setUp() {
        // Create three blocks with varying dimensions (width, height)
        Block block1 = new EmptyBlock(10.0, 11.0);
        Block block2 = new EmptyBlock(20.0, 22.0);
        Block block3 = new EmptyBlock(30.0, 33.0); // This block is the widest and tallest

        // The container uses a 1-row, 3-column grid arrangement
        GridArrangement arrangement = new GridArrangement(1, 3);
        container = new BlockContainer(arrangement);
        container.add(block1);
        container.add(block2);
        container.add(block3);
    }

    /**
     * Verifies that when arranging with a height range and no width constraint,
     * the grid's width is based on the widest block and its height respects the
     * specified range.
     */
    @Test
    @DisplayName("Arrange with height range and no width constraint should calculate correct size")
    void arrangeWithHeightRangeAndNoWidthConstraint_shouldCalculateCorrectSize() {
        // Arrange: Define a constraint with no width limit but a height range of [40.0, 60.0].
        // The container's natural height from its blocks is 33.0 (the max block height).
        Range heightRange = new Range(40.0, 60.0);
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeHeight(heightRange);

        // Act: Arrange the container with the given constraint.
        Size2D arrangedSize = container.arrange(null, constraint);

        // Assert: Verify the calculated width and height.

        // The grid has 3 columns. The arrangement logic makes each column as wide as the
        // widest block in the grid (30.0).
        // Expected width = 3 columns * 30.0 = 90.0.
        double expectedWidth = 90.0;
        assertEquals(expectedWidth, arrangedSize.width, EPSILON,
                "Width should be 3 times the width of the widest block");

        // The natural height (33.0) is below the constraint range [40.0, 60.0].
        // Therefore, the final height should be adjusted to the range's lower bound.
        double expectedHeight = 40.0;
        assertEquals(expectedHeight, arrangedSize.height, EPSILON,
                "Height should be constrained to the lower bound of the specified range");
    }
}