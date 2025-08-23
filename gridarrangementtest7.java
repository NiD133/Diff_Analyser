package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Creates a container with a 1x3 grid arrangement and three blocks of varying sizes.
     *
     * @return A pre-configured {@link BlockContainer} for testing.
     */
    private BlockContainer createContainerWithSingleRowGrid() {
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
     * Tests the arrangement of blocks when the constraint specifies a range for the
     * width and a fixed value for the height.
     * <p>
     * The expected behavior is that the arrangement will use the maximum available
     * width from the range and the specified fixed height. This corresponds to the
     * internal arrangeRF() method.
     */
    @Test
    void arrange_withRangeWidthAndFixedHeight_shouldUseMaxWidthAndFixedHeight() {
        // Arrange: Create a container and a constraint with a width range and a fixed height.
        BlockContainer container = createContainerWithSingleRowGrid();
        Range widthRange = new Range(40.0, 60.0);
        double fixedHeight = 100.0;
        RectangleConstraint constraint = new RectangleConstraint(widthRange, fixedHeight);

        // Act: Arrange the container with the given constraint.
        Size2D arrangedSize = container.arrange(null, constraint);

        // Assert: Verify that the resulting size uses the maximum width from the
        // range and the fixed height from the constraint.
        double expectedWidth = 60.0;
        assertEquals(expectedWidth, arrangedSize.width, EPSILON,
                "Width should be the maximum of the range constraint");

        double expectedHeight = 100.0;
        assertEquals(expectedHeight, arrangedSize.height, EPSILON,
                "Height should match the fixed constraint");
    }
}