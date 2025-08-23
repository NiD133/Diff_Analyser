package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * This test verifies the behavior of the arrange method when both the width
     * and height are constrained by a Range. The name 'RR' in the original test
     * likely stood for 'Range-Range'.
     *
     * The test sets up a 1x3 grid. The natural width is the sum of the block
     * widths, and the natural height is the maximum of the block heights.
     * The final arranged size should be the natural size, but constrained
     * to fit within the provided width and height ranges.
     */
    @Test
    @DisplayName("Arrange with width and height ranges should return size constrained by ranges")
    void arrangeWithWidthAndHeightRanges_ShouldReturnSizeConstrainedByRanges() {
        // Arrange
        // Create three blocks to be arranged in a 1x3 grid.
        Block block1 = new EmptyBlock(10.0, 11.0);
        Block block2 = new EmptyBlock(20.0, 22.0);
        Block block3 = new EmptyBlock(30.0, 33.0);

        BlockContainer container = new BlockContainer(new GridArrangement(1, 3));
        container.add(block1);
        container.add(block2);
        container.add(block3);

        // The natural width is the sum of block widths (10 + 20 + 30 = 60).
        // The natural height is the max of block heights (max(11, 22, 33) = 33).
        double naturalWidth = 60.0;
        double naturalHeight = 33.0;

        // Define constraints that will test the upper bound of the width and the lower bound of the height.
        Range widthRange = new Range(40.0, 60.0);
        Range heightRange = new Range(50.0, 70.0);
        RectangleConstraint constraint = new RectangleConstraint(widthRange, heightRange);

        // Act
        Size2D actualSize = container.arrange(null, constraint);

        // Assert
        // The expected width is the natural width (60.0) constrained by the width range [40.0, 60.0], which is 60.0.
        double expectedWidth = widthRange.constrain(naturalWidth);
        assertEquals(60.0, expectedWidth, "Constrained width should match the upper bound of the range.");

        // The expected height is the natural height (33.0) constrained by the height range [50.0, 70.0], which is 50.0.
        double expectedHeight = heightRange.constrain(naturalHeight);
        assertEquals(50.0, expectedHeight, "Constrained height should match the lower bound of the range.");

        assertEquals(expectedWidth, actualSize.width, EPSILON);
        assertEquals(expectedHeight, actualSize.height, EPSILON);
    }
}