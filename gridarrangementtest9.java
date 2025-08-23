package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Creates a container with a 1x3 grid arrangement, containing three blocks of different sizes.
     * <ul>
     * <li>Block 1: 10x11</li>
     * <li>Block 2: 20x22</li>
     * <li>Block 3: 30x33</li>
     * </ul>
     * @return A pre-configured BlockContainer for testing.
     */
    private BlockContainer createContainerWithSingleRowOfBlocks() {
        Block block1 = new EmptyBlock(10, 11);
        Block block2 = new EmptyBlock(20, 22);
        Block block3 = new EmptyBlock(30, 33);

        BlockContainer container = new BlockContainer(new GridArrangement(1, 3));
        container.add(block1);
        container.add(block2);
        container.add(block3);
        return container;
    }

    /**
     * Tests the arrangement of blocks in a single row where the width is constrained
     * by a range and the height is unconstrained. The arrangement should use the
     * upper bound of the width range, as it matches the natural width of the blocks.
     */
    @Test
    public void arrange_withWidthRangeAndNoHeightConstraint_returnsCorrectSize() {
        // Arrange
        BlockContainer container = createContainerWithSingleRowOfBlocks();
        // The constraint specifies a width between 40.0 and 60.0, with no height limit.
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeWidth(new Range(40.0, 60.0));

        // Act
        Size2D actualSize = container.arrange(null, constraint);

        // Assert
        // The expected width is the sum of the individual block widths (10 + 20 + 30 = 60),
        // which falls at the upper bound of the allowed range.
        double expectedWidth = 60.0;

        // The expected height is the maximum height of the blocks in the single row (max(11, 22, 33) = 33).
        double expectedHeight = 33.0;

        assertEquals(expectedWidth, actualSize.width, EPSILON, "Width should be the sum of block widths");
        assertEquals(expectedHeight, actualSize.height, EPSILON, "Height should be the max block height in the row");
    }
}